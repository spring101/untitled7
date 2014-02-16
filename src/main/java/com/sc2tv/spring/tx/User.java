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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User extends Sc2TvUser {

    private String chat_token;
    private WebClient webClient;

    public User() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    private boolean loggedIn = false;

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public User(Sc2TvUser sc2TvUser) {
        super(sc2TvUser);
        webClient = new WebClient();
    }

    public void setProxyUnit(ProxyUnit proxyUnit) {
        webClient.setProxyUnit(proxyUnit);
    }

    public User(ProxyUnit proxyUnit) {
        super();
        webClient = new WebClient();
        webClient.setProxyUnit(proxyUnit);
    }
    public void sendBan(){}
    private String getUrl(String url, int vote) {
        String urlUP = "", urlDOWN = "";
        String toReturn = "";
        String response = "";
        Document doc;
        try {
            response = webClient.executeGet(url);
        } catch (Exception exp) {

        }
        doc = Jsoup.parse(response);
        urlUP = doc.select("a.vud-link-up").attr("href");
        urlDOWN = doc.select("a.vud-link-down").attr("href");
        if (vote == 1) {
            toReturn = urlUP;
        } else if (vote == -1) {
            toReturn = urlDOWN;
        } else
            toReturn = "";
        return toReturn;
    }

    private void vote(String url, int vote) {
        //System.out.println(url);
        String voteUrl = getUrl(url, vote);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("js", "1"));
        parameters.add(new BasicNameValuePair("ctools_ajax", "1"));
        webClient.executePost("http://sc2tv.ru/" + voteUrl, parameters);
    }

    public void voteUp(String url) {
        vote(url, 1);
    }

    public void voteDown(String url) {
        vote(url, -1);
    }
    public void voteBan(String banUserId, String userName, String messageId, String reasonId){
        String task = "CitizenVoteForUserBan";
        String token = chat_token;
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("banUserId", banUserId));
        params.add(new BasicNameValuePair("messageId", messageId));
        params.add(new BasicNameValuePair("reasonId", reasonId));
        params.add(new BasicNameValuePair("task", task));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("userName", userName));

        System.out.println(String.format("%s - %s - %s", getUsername(), userName, token));
        webClient.executePost("http://chat.sc2tv.ru/gate.php", params);
    }
    public void logIn() {
        String response = webClient.executeGet("http://sc2tv.ru", new HashMap<String, String>());
        Document doc = Jsoup.parse(response);
        Elements elm = doc.select("form[id=user-login-form]");
        String form_build_id = elm.select("input[name=form_build_id]").val();
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("destination", "node"));

        parameters.add(new BasicNameValuePair("form_build_id", form_build_id));
        parameters.add(new BasicNameValuePair("form_id", "user_login_block"));
        parameters.add(new BasicNameValuePair("name", getUsername()));
        parameters.add(new BasicNameValuePair("op", "Вход"));
        parameters.add(new BasicNameValuePair("pass", getPassword()));
        webClient.executePost("http://sc2tv.ru/all", parameters);
        Map<String, String> params = new HashMap<>();
        params.put("task", "GetUserInfo");
        String respToken = webClient.executeGet("http://chat.sc2tv.ru/gate.php", params);
        if (respToken == "") {
            return;
        }
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(respToken);
        } catch (ParseException e) {
        }
        JSONObject jsonObj = null;
        try {
            jsonObj = (JSONObject) obj;
        } catch (ClassCastException exp) {
            return;
        }
        String token = (String) jsonObj.get("token");
        if (token != null) {
            chat_token = token;
            loggedIn = true;
            return;
        }
        loggedIn = false;
    }

    ;

    public void sendMessage(String message, String channelId) {
        if (chat_token == null)
            return;
        List<NameValuePair> params = new ArrayList<>();
        try {
            message = URLDecoder.decode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.add(new BasicNameValuePair("task", "WriteMessage"));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("channel_id", channelId));
        params.add(new BasicNameValuePair("token", chat_token));
        webClient.executePost("http://chat.sc2tv.ru/gate.php", params);
    }
}
