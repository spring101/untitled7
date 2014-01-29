package com.sc2tv.spring.tx;


import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.user.UserManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloApp  {
    public static Channels channels = new Channels();
    public static WebClient webClient;
    public static ProxyManager proxyManager;

    public static void main(String[] args) throws IOException {
       ApplicationContext ctx =
                new ClassPathXmlApplicationContext("");

       final UserManager userManager =
                (UserManager) ctx.getBean("userManagerImpl");
       proxyManager =
                (ProxyManager) ctx.getBean("proxyManagerImpl");

       proxyManager.setPool(200);
        Executor executor = Executors.newFixedThreadPool(proxyManager.getPool());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                proxyManager.run();
            }
        });

        Sc2TvUser sc2TvUser1 = null;
        for(Sc2TvUser sc2TvUser:userManager.getUsers()){

            sc2TvUser1 = sc2TvUser;
                break;

        }
        ProxyUnit proxyUnit = null;
        while(proxyUnit==null){
            proxyUnit =proxyManager.getAvaliable();
        };
        proxyUnit.getHost();
        proxyUnit.getPort();
        User user = new User(sc2TvUser1, new ProxyUnit(proxyUnit.getHost(),proxyUnit.getPort()));

        user.sendMessage("Привет, Гарик :-)", channels.getChannelIdByStreamerName("Garklav"));
       // User user = new User(sc2TvUser1, new ProxyUnit(sc2TvUser1.getProxy().split(":")[0],Integer.parseInt(sc2TvUser1.getProxy().split(":")[1])));

        String line1;
        int count=0;

       /*BufferedReader br = new BufferedReader(new FileReader("C:\\1.txt"));
       String line;
       while ((line = br.readLine()) != null) {
            String[] params = line.split(":");
            String host = params[0];
            String port = params[1];
                ProxyUnit proxyUnit = new ProxyUnit();
                proxyUnit.setHost(host);
                proxyUnit.setPort(Integer.parseInt(port));
           try{
                proxyManager.insertProxyUnit(proxyUnit);
           }
           catch(Exception exp){};
        }
        br.close();
       Executor executor = Executors.newFixedThreadPool(proxyManager.getPool());
       executor.execute(new Runnable() {
            @Override
            public void run() {
                proxyManager.run();
            }
       });
        while(true){
        if(proxyManager.getAvaliableProxies().size()>100)
        for(ProxyUnit proxyUnit:proxyManager.getProxyUnitList())System.out.println(String.format("%s:%s", proxyUnit.getHost(), proxyUnit.getPort()));}

*/
    //    StrawpoolImpl strawpool = new StrawpoolImpl("CAH4EC");

      //  strawpool.setProxyManager(proxyManager);
       // strawpool.scanAll(50, -1);
    }


    private static String verifyEmail(String email, String emailpass) {
        webClient = new WebClient();
        List<NameValuePair> params = new ArrayList<>();
        String[] emailSplit = email.split("@");
        params.add(new BasicNameValuePair("Domain", emailSplit[1]));
        params.add(new BasicNameValuePair("Login", emailSplit[0]));
        params.add(new BasicNameValuePair("Password", emailpass));
        params.add(new BasicNameValuePair("lang", "ru_RU"));
        params.add(new BasicNameValuePair("login_from", ""));
        params.add(new BasicNameValuePair("new_auth_form", "1"));
        params.add(new BasicNameValuePair("page", ""));
        params.add(new BasicNameValuePair("post", ""));
        params.add(new BasicNameValuePair("saveauth", "1"));
        params.add(new BasicNameValuePair("setLang", "ru_RU"));
        try {
            webClient.executePost("https://auth.mail.ru/cgi-bin/auth", params);
            String output = webClient.executeGet("https://e.mail.ru/messages/inbox/", new HashMap<String, String>());

            output = output.replace("\\u", "u");
                Pattern pattern = Pattern.compile("(?<=u043Fu0430u0440u043Eu043Bu044C:\\s)(\\w+)");
            Matcher matcher = pattern.matcher(output);
            if(matcher.find()){
                String key = matcher.group(1);
                return key;
            } else {}
        } catch (Exception e) {
              e.printStackTrace();
        }
        return "";
    }
}
