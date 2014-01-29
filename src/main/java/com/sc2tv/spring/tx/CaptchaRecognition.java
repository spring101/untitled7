package com.sc2tv.spring.tx;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xerces.impl.dv.util.Base64;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaptchaRecognition {
    private String imageUrl;
    private byte[] bytes;
    private String apiToken = "893298284603e7a13e4e0d42140b6935";
    WebClient webClient = new WebClient();
    public CaptchaRecognition(byte[] bytes) throws IOException {
        this.bytes = bytes;
    }
    public String process() throws IOException, URISyntaxException {
        return solveCaptchaMulty();
    }

    private String solveCaptchaMulty() throws IOException, URISyntaxException {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("method", "base64"));
        pairs.add(new BasicNameValuePair("key", apiToken));
        pairs.add(new BasicNameValuePair("body",  (new Base64()).encode(bytes)));
        String captchaStatus = webClient.executePost("http://antigate.com/in.php", pairs);
        if (captchaStatus.matches("OK\\|.*")) {
            String captchaId = captchaStatus.split("\\|")[1];

            while (true) {
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                captchaStatus = getCaptchaStatus(captchaId);
                //log("Captcha status: " + captchaStatus);
                if (captchaStatus.matches("OK\\|.*")) {
                    System.out.println(captchaStatus);
                    return captchaStatus.split("\\|")[1];
                } else if (captchaStatus.equals("CAPCHA_NOT_READY")){
                    continue;
                } else {
                    System.out.println("Error captcha status: " + captchaStatus);
                }
            }
        } else {
            System.out.println("Error captcha status: " + captchaStatus);
        }
        return "";
    }
    private String getCaptchaStatus(String id) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("key", apiToken);
        params.put("action", "get");
        params.put("id", id);
        return webClient.executeGet("http://antigate.com/res.php", params);
    }
}
