package com.sc2tv.spring.tx.service.strawpool.imp;

import com.sc2tv.spring.tx.CaptchaRecognition;
import com.sc2tv.spring.tx.HelloApp;
import com.sc2tv.spring.tx.Scanner;
import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.service.strawpool.Strawpool;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrawpoolImpl implements Strawpool{
    Scanner scanner;
    List<String> task;

    public StrawpoolImpl(String channel) {
        scanner = new Scanner(getId(channel));
        task = new ArrayList<>();
    }
    ProxyManager proxyManager;
    private WebClient webClient = new WebClient();
    String channel;
    public void setProxyManager(ProxyManager proxyManager)
    {
        this.proxyManager = proxyManager;
    };

    @Override
    public void startVoteStrawpool(String url, final int[] voteFor, int threads) {
        WebClient webClient2 = new WebClient();
        webClient2.setProxyManager(proxyManager);
        String resp = webClient2.executeGet(url, new HashMap<String, String>());
        org.jsoup.nodes.Document document = Jsoup.parse(resp);
        final int nums = document.select("div[class=pollOption]").size();
        final Random random = new Random();
        final Integer pool = Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<ProxyUnit> proxyUnits = proxyManager.getProxyUnitList();
        if(voteFor[0]!=-1)
            for(final ProxyUnit proxyUnit: proxyUnits){
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        WebClient webClient1 = new WebClient();
                        webClient1.setProxyUnit(proxyUnit);
                        int toVoteFor = voteFor[random.nextInt(voteFor.length)];
                        webClient1.sendPostStrawpool(toVoteFor, Integer.parseInt(pool.toString()));
                    }
                });
            }
        else {
            final int toVoteFor = random.nextInt(nums);
            for(final ProxyUnit proxyUnit: proxyUnits){
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        WebClient webClient1 = new WebClient();
                        webClient1.setProxyUnit(proxyUnit);
                        webClient1.sendPostStrawpool(toVoteFor, Integer.parseInt(pool.toString()));
                    }
                });
            }
        }
    }
    @Override
    public void startVoteRupool(final String url, final int[] voteFor, int threads) {
        final int[] count = {0};
        int maxCount = 300;
        WebClient webClient2 = new WebClient();
        webClient2.setProxyManager(proxyManager);
        String resp = webClient2.executeGet(url, new HashMap<String, String>());
        org.jsoup.nodes.Document document = Jsoup.parse(resp);
        final int nums = document.select("input[type=radio]").size();
        final Random random = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<ProxyUnit> proxyUnits = proxyManager.getProxyUnitList();
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        if(voteFor[0]!=-1)
        for(final ProxyUnit proxyUnit: proxyUnits){
            if(count[0] >=maxCount)
                return;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    WebClient webClient1 = new WebClient();
                    String poll_id = url.substring(url.lastIndexOf("/")+1, url.indexOf(".html")-2);
                    String vote = String.valueOf(voteFor[random.nextInt(voteFor.length)]+1);
                    webClient1.setProxyUnit(proxyUnit);
                    String toCompute = webClient1.sendPostRupool(poll_id, vote);
                    if(toCompute=="")
                        return;
                    String kt = toCompute.substring(toCompute.indexOf("&kt=") + 4, toCompute.indexOf("')</SCRIPT>"));
                    toCompute = toCompute.substring(toCompute.indexOf("var"), toCompute.indexOf("location"));
                    try{
                        scriptEngine.eval(toCompute);
                        Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
                        String param = toCompute.substring(toCompute.indexOf("var ") + "var ".length(), toCompute.indexOf("="));
                        HashMap<String, String> params = new HashMap<>();
                        params.put("id", poll_id);
                        params.put("kv", bindings.get(param).toString());
                        params.put("kt", kt);

                        String outPut = webClient1.executeGet("http://www.rupoll.com/voteconfirm.php", params);
                        if(outPut.contains("image")){
                            params.remove("id");
                            byte[] image = webClient1.executeGetByte("http://www.rupoll.com/checkimage.php", params);
                            List<NameValuePair> pairs = new ArrayList<>();
                            pairs.add(new BasicNameValuePair("SubmitButton", "%CF%EE%E4%F2%E2%E5%F0%E4%E8%F2%FC+%CA%EE%E4"));
                            pairs.add(new BasicNameValuePair("code", new CaptchaRecognition(image).process()));
                            pairs.add(new BasicNameValuePair("id", String.valueOf(poll_id)));
                            pairs.add(new BasicNameValuePair("kt", kt));
                            pairs.add(new BasicNameValuePair("kv", bindings.get(param).toString()));
                            params.put("SubmitButton", "%CF%EE%E4%F2%E2%E5%F0%E4%E8%F2%FC+%CA%EE%E4");
                            params.put("code", new CaptchaRecognition(image).process());
                            count[0]++;
                            System.out.println(webClient1.executePost("http://www.rupoll.com/voteconfirm.php", pairs));
                        }
                        System.out.println(String.format("Rupool done: pool: %s  - vote: %d", poll_id, voteFor));
                    }catch (Exception exp){
                        exp.printStackTrace();
                    };
                }
            });
        }
        else{
            final int toVoteFor = random.nextInt(nums);
            for(final ProxyUnit proxyUnit: proxyUnits){
                if(count[0] >=maxCount)
                    return;
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        WebClient webClient1 = new WebClient();
                        String poll_id = url.substring(url.lastIndexOf("/")+1, url.indexOf(".html")-2);
                        String vote = String.valueOf(toVoteFor+1);
                        webClient1.setProxyUnit(proxyUnit);
                        String toCompute = webClient1.sendPostRupool(poll_id, vote);
                        if(toCompute=="")
                            return;
                        String kt = toCompute.substring(toCompute.indexOf("&kt=") + 4, toCompute.indexOf("')</SCRIPT>"));
                        toCompute = toCompute.substring(toCompute.indexOf("var"), toCompute.indexOf("location"));
                        try{
                            scriptEngine.eval(toCompute);
                            Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
                            String param = toCompute.substring(toCompute.indexOf("var ") + "var ".length(), toCompute.indexOf("="));
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", poll_id);
                            params.put("kv", bindings.get(param).toString());
                            params.put("kt", kt);

                            String outPut = webClient1.executeGet("http://www.rupoll.com/voteconfirm.php", params);
                            if(outPut.contains("image")){
                                params.remove("id");
                                byte[] image = webClient1.executeGetByte("http://www.rupoll.com/checkimage.php", params);
                                List<NameValuePair> pairs = new ArrayList<>();
                                pairs.add(new BasicNameValuePair("SubmitButton", "%CF%EE%E4%F2%E2%E5%F0%E4%E8%F2%FC+%CA%EE%E4"));
                                pairs.add(new BasicNameValuePair("code", new CaptchaRecognition(image).process()));
                                pairs.add(new BasicNameValuePair("id", String.valueOf(poll_id)));
                                pairs.add(new BasicNameValuePair("kt", kt));
                                pairs.add(new BasicNameValuePair("kv", bindings.get(param).toString()));
                                params.put("SubmitButton", "%CF%EE%E4%F2%E2%E5%F0%E4%E8%F2%FC+%CA%EE%E4");
                                params.put("code", new CaptchaRecognition(image).process());
                                count[0]++;
                                System.out.println(webClient1.executePost("http://www.rupoll.com/voteconfirm.php", pairs));
                            }
                            System.out.println(String.format("Rupool done: pool: %s  - vote: %d", poll_id, voteFor));
                        }catch (Exception exp){
                            exp.printStackTrace();
                        };
                    }
                });
            }
        }
    }
    public void scanSingle(final int threads, final int[] voteFor)
    {
        final Pattern pattern = Pattern.compile("strawpoll.me/\\d*");
        final Pattern pattern2 = Pattern.compile("rupoll.com/\\w*.html");
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() { public void run() {
            while (true){//"http://www.rupoll.com/nnhypwepsr.html"
                Matcher matcher = pattern.matcher(scanner.getResponse().replace("\\", ""));
                Matcher matcher2 = pattern2.matcher(scanner.getResponse().replace("\\", ""));
                boolean STR = matcher.find();
                boolean RUP = matcher2.find();
                while (STR||RUP)
                {
                    if(STR){
                    String strawpool = "http://www."+matcher.group();
                    if(!task.contains(strawpool)){
                        task.add(strawpool);
                        startVoteStrawpool(strawpool, voteFor, threads);
                    }}
                    if(RUP){
                        String rupool = "http://www."+matcher2.group();
                        if(!task.contains(rupool)){
                            task.add(rupool);
                            startVoteRupool(rupool, voteFor, threads);
                        }}
                    STR = matcher.find();
                    RUP = matcher2.find();
                };
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } });
    }
    public void scanAll(final int threads, final int[] voteFor)
    {
        final Pattern pattern = Pattern.compile("strawpoll.me/\\d*");
        Executor executor = Executors.newSingleThreadExecutor();
        final List<Scanner> scanners = new ArrayList<>();
        for(String channel: getIds()){
            scanners.add(new Scanner(channel));
        };
        executor.execute(new Runnable() { public void run() {
            while (true){
                for(Scanner scanner1: scanners){
                Matcher matcher = pattern.matcher(scanner1.getResponse().replace("\\", ""));
                boolean STR = matcher.find();
                if(STR){
                    String strawpool = "http://www."+matcher.group();
                    if(!task.contains(strawpool)){
                        task.add(strawpool);
                        System.out.println(String.format("Starting vote STR in chat: %s - %s - STR: %s", HelloApp.channels.getStreamerNameByChannelId(scanner1.getChatId()), "http://sc2tv.ru/content/"+HelloApp.channels.getTitleByChannelId(scanner1.getChatId()), strawpool));
                        startVoteStrawpool(strawpool, voteFor, threads);
                    }}
            }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }} });
    }

    public String[] getIds(){
        String resp = "";
        List<String> toReturn = new ArrayList<>();
        resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channels.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("channel");

        objects.remove(0);
        for(Object object: objects){
            toReturn.add((String) ((JSONObject) object).get("channelId"));
        }
        return toReturn.toArray(new String[toReturn.size()]);
    }

    public String getId(String channel){
        String resp = "";
        resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channels.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("channel");
        objects.remove(0);
        for(Object object: objects){
           if(((String)((JSONObject)object).get("streamerName")).contains(channel))
               return (String) ((JSONObject)object).get("channelId");
        }
        return "";
    }
}
