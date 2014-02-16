package com.sc2tv.spring.tx.registraton.impl;

import com.sc2tv.spring.tx.EmailManager.EmailManager;
import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.model.EmailClass;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.registraton.RegistrationManager;
import com.sc2tv.spring.tx.service.CaptchaRecognition;
import com.sc2tv.spring.tx.user.UserManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RegistrationManagerImpl implements RegistrationManager {
    WebClient webClient;
    @Autowired
    CaptchaRecognition captchaRecognition;
    @Autowired
    ProxyManager proxyManager;
    @Autowired
    UserManager userManager;
    @Autowired
    EmailManager emailManager;

    public RegistrationManagerImpl() {
    }

    public boolean register(String username) {


        boolean toReturn = false;
        User user;
        if (userManager.getUser(username) != null) {
            return false;
        }
        EmailClass emailClass;
        emailClass = emailManager.getFirstNotUsed();
        while (!emailManager.checkAvaliable(emailClass)) {
            emailClass.setUsed(1);
            emailManager.inrest(emailClass);
            emailClass = emailManager.getFirstNotUsed();
        }
        if (emailClass == null) {
            return false;
        }
        //   ProxyUnit proxyUnit = proxyManager.getAvaliable();
        //if (proxyUnit == null) {
        //      return false;
        //   }
        user = new User();
        user.setUsername(username);
        user.setEmail(emailClass.getEmail());
        user.setEmailPass(emailClass.getPassword());
        int result = 0;
        try {
            result = registerUser(user, 1);
        } catch (IOException e) {
        } catch (URISyntaxException e) {
        }
        int counter = 0;
        if ((result >= 1) && counter != 20) {
            if(result == 2)
                if(userManager.getUser(username)!=null)
                    return false;
            String password = "";
            while ((password = emailManager.getPassword(emailClass)) == "") {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
            }
            ;
            if (counter == 20) {
                System.out.println("so much checks");
                return false;
            }
            System.out.println("Password is " + password);
            user.setPassword(password);
            emailClass.setUsed(1);
            emailManager.inrest(emailClass);
            userManager.insertUser(user);
        }
        return toReturn;
    }
    int counter = 0;
    @Override
    public void checkMailsForRegistered(){
        List<EmailClass> emailClasses =emailManager.getEmailList();
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        int counter = 0;
        for(final EmailClass emailClass: emailClasses){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    check(emailClass);
                }
            });
        }
    }
    @Override
    public void check(EmailClass emailClass){
        System.out.println(counter++);
        if(emailManager.checkAvaliable(emailClass)!=true){
            String name = "";
            try {
                name = emailManager.getName(emailClass);
                name = name.replace("\r", "").replace("\n", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(name == "")
                return;
            else if(userManager.getUser(name) == null){
                Sc2TvUser user = new Sc2TvUser();
                user.setUsername(name);
                user.setEmail(emailClass.getEmail());
                user.setEmailPass(emailClass.getPassword());
                user.setPassword(emailManager.getPassword(emailClass));
                userManager.insertUser(user);
                emailClass.setUsed(1);
                emailManager.inrest(emailClass);
                System.out.println("User+Email inserted");
            }else if(emailClass.getUsed()!=1){
                emailClass.setUsed(1);
                emailManager.inrest(emailClass);
                System.out.println("Email inserted");

            }
        }
    }
    public int registerUser(User user, int tries) throws IOException, URISyntaxException {
        if (tries <= 0)
            return 0;
        String captcha_response;
        String captcha_sid;
        String captcha_token;
        String form_build_id;
        String form_id = "user_register";
        String mail = user.getEmail();
        String name = user.getUsername();
        String op = "Создать аккаунт";
        List<NameValuePair> params = new ArrayList<>();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webClient = new WebClient();

        webClient.setProxyUnit(proxyManager.getAvaliable());
        String response = webClient.executeGet("http://sc2tv.ru/user/register", new HashMap<String, String>());
        Document doc = Jsoup.parse(response);
        Elements elm = doc.select("form[id=user-register]");
        String imageUrl = "http://sc2tv.ru" + doc.select("div[class=captcha").select("img").attr("src") + ".img";
        if(!imageUrl.contains("image_captcha"))
            return registerUser(user, 0);
        System.out.println(imageUrl);
        byte[] image = webClient.executeGetByte(imageUrl);
        captcha_response = captchaRecognition.solveCaptchaMulty(image);
        if(captcha_response.isEmpty())
            return registerUser(user, 0);
        captcha_sid = elm.select("input[id=edit-captcha-sid]").val();
        captcha_token = elm.select("input[id=edit-captcha-token]").val();
        form_build_id = elm.select("input[name=form_build_id]").val();
        params.add(new BasicNameValuePair("name", user.getUsername()));
        params.add(new BasicNameValuePair("mail", user.getEmail()));
        params.add(new BasicNameValuePair("form_build_id", form_build_id));
        params.add(new BasicNameValuePair("form_id", form_id));
        params.add(new BasicNameValuePair("captcha_sid", captcha_sid));
        params.add(new BasicNameValuePair("captcha_token", captcha_token));
        params.add(new BasicNameValuePair("captcha_response", captcha_response));
        params.add(new BasicNameValuePair("op", op));
        response = webClient.executePost("http://sc2tv.ru/user/register", params);
        doc = Jsoup.parse(response);
        Element eml = doc.select("div[class=messages error]").get(0);
        if (doc.select("div[class=messages error]").size() > 0) {
            if (eml.text().contains("is already taken")){
                return 0;
            }
            return registerUser(user, 0);
        } else{
            System.out.println("sucsess");

            return 1;
        }
    }
}
