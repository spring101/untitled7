package com.sc2tv.spring.tx;

import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User{
    private String chat_token;
    private WebClient webClient;
    private ProxyUnit proxyUnit;
    private Sc2TvUser sc2TvUser;
    private boolean loggedIn = false;
    public WebClient getWebClient() {
        return webClient;
    }
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
    public User(Sc2TvUser sc2TvUser, ProxyUnit proxyUnit) {
        this.sc2TvUser = sc2TvUser;
        this.proxyUnit = proxyUnit;
        webClient = new WebClient();
        webClient.setProxyUnit(proxyUnit);
        logIn();
    }

    private void vote(String channelTitle, int vote){
        String response = webClient.executeGet(String.format("http://sc2tv.ru/content/%s", channelTitle), new HashMap<String, String>());
        Document doc = Jsoup.parse(response);
        String url;
        if(vote==-1){
            url = doc.select("a[class=vud-link-up ctools-use-ajax ctools-use-ajax-processed]").attr("href");
        }
        else{
            url = doc.select("a[class=vud-link-down ctools-use-ajax ctools-use-ajax-processed]").attr("href");
        }
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("js", "1"));
        parameters.add(new BasicNameValuePair("ctools_ajax", "1"));
        webClient.executePost(url, parameters);
    }
    public void voteUp(String channelTitle){
        vote(channelTitle, 1);
    }
    public void voteDown(String channelTitle){
        vote(channelTitle, -1);
    }
    private void logIn(){
        String response = webClient.executeGet("http://sc2tv.ru", new HashMap<String, String>());
        Document doc = Jsoup.parse(response);
        Elements elm = doc.select("form[id=user-login-form]");
        String form_build_id = elm.select("input[name=form_build_id]").val();
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("form_build_id", form_build_id));
        parameters.add(new BasicNameValuePair("form_id", "user_login_block"));
        parameters.add(new BasicNameValuePair("name", sc2TvUser.getUsername()));
        parameters.add(new BasicNameValuePair("op", "Вход"));
        parameters.add(new BasicNameValuePair("pass", sc2TvUser.getPassword()));
        System.out.println(webClient.executePost("http://sc2tv.ru/all", parameters));
        Map<String, String> params = new HashMap<>();
        params.put("task", "GetUserInfo");
        String respToken = webClient.executeGet("http://chat.sc2tv.ru/gate.php", params);
        if(respToken==""){
            System.out.println("ERROR getting TOKEN");
            return;
        }
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(respToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = null;
        try{
        jsonObj = (JSONObject) obj;
        } catch(ClassCastException exp){
            System.out.println((String)obj);
            exp.printStackTrace();
            return;
        }
        String token = (String) jsonObj.get("token");
        if(token!=null){
            chat_token = token;
            loggedIn = true;
            return;
        }
        loggedIn = false;
    };
    public void sendMessage(String message, String channelId){
        if(chat_token==null)
            return;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("task", "WriteMessage"));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("channel_id", channelId));
        params.add(new BasicNameValuePair("token", chat_token));
        webClient.executePost("http://chat.sc2tv.ru/gate.php", params);
    }
}
