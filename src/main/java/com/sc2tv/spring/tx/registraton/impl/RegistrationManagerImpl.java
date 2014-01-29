package com.sc2tv.spring.tx.registraton.impl;

import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.registraton.RegistrationManager;
import com.sc2tv.spring.tx.WebClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationManagerImpl implements RegistrationManager {
    Sc2TvUser sc2TvUser;
    WebClient webClient;

    public RegistrationManagerImpl(Sc2TvUser sc2TvUser, ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
        this.sc2TvUser = sc2TvUser;
        webClient = new WebClient();
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public void setProxyManager(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    ProxyManager proxyManager;

    public RegistrationManagerImpl(Sc2TvUser sc2TvUser) {
        this.sc2TvUser = sc2TvUser;
        webClient = new WebClient();
        webClient.setProxyManager(proxyManager);
    }
    private ProxyUnit getAvaliableProxy()
    {
        return proxyManager.getAvaliable();
    }
    @Override
    public int registerUser(int tries) throws IOException, URISyntaxException {
        if(tries<=0)
            return 0;
        String captcha_response;
        String captcha_sid;
        String captcha_token;
        String form_build_id;
        String form_id = "user_register";
        String mail = sc2TvUser.getEmail();
        String name = sc2TvUser.getUsername();
        String op = "Создать аккаунт";
        ProxyUnit proxyUnit = getAvaliableProxy();
        webClient.setProxyUnit(proxyUnit);
        List<NameValuePair> params = new ArrayList<>();
        String response = webClient.executeGet("http://sc2tv.ru/user/register", null);
        Document doc = Jsoup.parse(response);
        Elements elm = doc.select("form[id=user-register]");
        System.out.println("Getting captcha1");
        captcha_response = "";//new CaptchaRecognition("http://sc2tv.ru"+doc.select("div[class=captcha").select("img").attr("src")+".img").process();
        captcha_sid = elm.select("input[id=edit-captcha-sid]").val();
        captcha_token = elm.select("input[id=edit-captcha-token]").val();
        form_build_id = elm.select("input[name=form_build_id]").val();
        params.add(new BasicNameValuePair("name", sc2TvUser.getUsername()));
        params.add(new BasicNameValuePair("mail", sc2TvUser.getEmail()));
        params.add(new BasicNameValuePair("form_build_id", form_build_id));
        params.add(new BasicNameValuePair("form_id", form_id));
        params.add(new BasicNameValuePair("captcha_sid", captcha_sid));
        params.add(new BasicNameValuePair("captcha_token", captcha_token));
        params.add(new BasicNameValuePair("captcha_response", captcha_response));
        params.add(new BasicNameValuePair("op", op));

        response = webClient.executePost("http://sc2tv.ru/user/register", params);
        doc = Jsoup.parse(response);
        Element eml = doc.select("div[class=messages error]").get(0);
        if(doc.select("div[class=messages error]").size()>0)
        {
            if(eml.text().contains("is already taken"))
                return 0;
            System.out.println(eml.text());
            return registerUser(tries--);
        }
        else
            return 1;
    }

    @Override
    public int setPassword(String url) throws IOException {
        String form_build_id;
        String form_id = "user_pass_reset";
        String op = "Вход";
        String urlToSetPassword;
        String pass1 = sc2TvUser.getPassword();
        String form_token;
        String response = webClient.executeGet(url, null);
        Document doc = Jsoup.parse(response);
        form_build_id = doc.select("input[name=form_build_id]").val();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("form_build_id", form_build_id));
        params.add(new BasicNameValuePair("form_id", form_id));
        params.add(new BasicNameValuePair("op", op));
        response = webClient.executePost(url+"/login", params);
        doc = Jsoup.parse(response);
        form_build_id = doc.select("input[name=form_build_id]").val();
        form_token = doc.select("input[name=form_token]").val();
        params.clear();
        params.add(new BasicNameValuePair("mail", sc2TvUser.getEmail()));
        params.add(new BasicNameValuePair("pass[pass1]", pass1));
        params.add(new BasicNameValuePair("pass[pass2]", pass1));
        params.add(new BasicNameValuePair("form_build_id", form_build_id));
        params.add(new BasicNameValuePair("form_token", form_token));
        params.add(new BasicNameValuePair("form_id", form_id));
        params.add(new BasicNameValuePair("op", "Сохранить"));
        urlToSetPassword = "http://sc2tv.ru" + doc.select("form[id=user-profile-form]").attr("action");
        response = webClient.executePost(urlToSetPassword, params);
        doc = Jsoup.parse(response);
        if(doc.select("div[class=messages status]").text().contains("Изменения сохранены."))
            return 1;
        else
            return 0;
    }
}
