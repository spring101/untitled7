package com.sc2tv.spring.tx.service;

import com.sc2tv.spring.tx.WebClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaptchaRecognition {
    private final static String apiToken = "893298284603e7a13e4e0d42140b6935";

    public CaptchaRecognition() {
    }

    public String getBalance() {
        String str = String.format("http://antigate.com/res.php?key=%s&action=getbalance", apiToken);
        return new WebClient().executeGet(str);
    }
    public String solveParts(String url, WebClient webClient){
        String captchaStatus = null;
        try {
            captchaStatus = webClient.executePostMultipart(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (captchaStatus.matches("OK\\|.*")) {
            String captchaId = captchaStatus.split("\\|")[1];
            while (true) {
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    captchaStatus = getCaptchaStatus(captchaId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //log("Captcha status: " + captchaStatus);
                if (captchaStatus.matches("OK\\|.*")) {
                    System.out.println(captchaStatus);
                    return captchaStatus.split("\\|")[1];
                } else if (captchaStatus.equals("CAPCHA_NOT_READY")) {
                    continue;
                } else {
                    System.out.println("Error captcha status: " + captchaStatus);
                    return "";
                }
            }
        } else {
            System.out.println("Error captcha status: " + captchaStatus);
            if(captchaStatus.equals("ERROR_NO_SLOT_AVAILABLE")){
                // System.out.println("Adjusting value");
            }
        }
        return "";
    }
    public String solveCaptchaMulty(byte[] bytes) throws IOException, URISyntaxException {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("method", "base64"));
        pairs.add(new BasicNameValuePair("key", apiToken));
        pairs.add(new BasicNameValuePair("body", (new Base64()).encode(bytes)));
        pairs.add(new BasicNameValuePair("regsense", "1"));
        String captchaStatus = new WebClient().executePost("http://antigate.com/in.php", pairs);
        if (captchaStatus.matches("OK\\|.*")) {
            String captchaId = captchaStatus.split("\\|")[1];
            while (true) {
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                captchaStatus = getCaptchaStatus(captchaId);
                //log("Captcha status: " + captchaStatus);
                if (captchaStatus.matches("OK\\|.*")) {
                    System.out.println(captchaStatus);
                    return captchaStatus.split("\\|")[1];
                } else if (captchaStatus.equals("CAPCHA_NOT_READY")) {
                    continue;
                } else {
                    System.out.println("Error captcha status: " + captchaStatus);
                    return "";
                }
            }
        } else {
            System.out.println("Error captcha status: " + captchaStatus);
            if(captchaStatus.equals("ERROR_NO_SLOT_AVAILABLE")){
               // System.out.println("Adjusting value");
            }
        }
        return "";
    }

    private String getCaptchaStatus(String id) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("key", apiToken);
        params.put("action", "get");
        params.put("id", id);
        return new WebClient().executeGet("http://antigate.com/res.php", params);
    }
}
