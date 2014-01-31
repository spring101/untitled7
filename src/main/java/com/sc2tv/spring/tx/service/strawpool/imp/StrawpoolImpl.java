package com.sc2tv.spring.tx.service.strawpool.imp;

import com.sc2tv.spring.tx.CaptchaRecognition;
import com.sc2tv.spring.tx.HelloApp;
import com.sc2tv.spring.tx.Scanner;
import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.service.strawpool.Strawpool;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

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
@Service
public class StrawpoolImpl implements Strawpool{
    Scanner scanner;
    List<String> task;

    public StrawpoolImpl(String channel) {
        scanner = new Scanner();
        task = new ArrayList<>();
    }
    public StrawpoolImpl() {
        task = new ArrayList<>();
    }
    ProxyManager proxyManager;

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
    public void startVoteRupool(final String url, final int[] voteFor, int threads, final boolean money) {
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
                        if(outPut.contains("image")&&money){
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
    public void scanSingle(String channel, final int threads, final int[] voteFor, final boolean money)
    {
        final Scanner scanner1 = new Scanner(channel);
        final Pattern pattern = Pattern.compile("strawpoll.me/\\d*");
        final Pattern pattern2 = Pattern.compile("rupoll.com/\\w*.html");
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() { public void run() {
            while (true){//"http://www.rupoll.com/nnhypwepsr.html"
                Matcher matcher = pattern.matcher(scanner1.getResponse().replace("\\", ""));
                Matcher matcher2 = pattern2.matcher(scanner1.getResponse().replace("\\", ""));
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
                            startVoteRupool(rupool, voteFor, threads, money);
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
    public void scanAll(final int[] options, final int threads, final boolean money)
    {
        final Pattern strpatt = Pattern.compile("strawpoll.me/\\d*");
        final Pattern ruppatt = Pattern.compile("rupoll.com/\\w*.html");
        Executor executor = Executors.newSingleThreadExecutor();
        final Scanner scanner = new Scanner();
        executor.execute(new Runnable() { public void run() {
            while (true){
                Matcher strmatch = strpatt.matcher(scanner.getResponse().replace("\\", ""));
                Matcher rupmatch = ruppatt.matcher(scanner.getResponse().replace("\\", ""));
                boolean STR = strmatch.find();
                boolean RUP = rupmatch.find();
                if(STR){
                    String strawpool = "http://www."+strmatch.group();
                    if(!task.contains(strawpool)){
                        task.add(strawpool);
                        System.out.println(String.format("Starting vote STR in chat: %s - %s - STR: %s", HelloApp.channels.getStreamerNameByChannelId(scanner.getChatId()), "http://sc2tv.ru/content/"+HelloApp.channels.getTitleByChannelId(scanner.getChatId()), strawpool));
                        startVoteStrawpool(strawpool, options, threads);
                    }}
                if(RUP){
                    String rupool = "http://www."+rupmatch.group();
                    if(!task.contains(rupool)){
                        task.add(rupool);
                        System.out.println(String.format("Starting vote STR in chat: %s - %s - STR: %s", HelloApp.channels.getStreamerNameByChannelId(scanner.getChatId()), "http://sc2tv.ru/content/"+HelloApp.channels.getTitleByChannelId(scanner.getChatId()), rupool));
                        startVoteRupool(rupool, options, threads, money);
                    }}
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } });
    }
}
