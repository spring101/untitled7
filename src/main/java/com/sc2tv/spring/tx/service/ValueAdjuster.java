package com.sc2tv.spring.tx.service;

import com.sc2tv.spring.tx.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValueAdjuster {
    @Autowired
    CaptchaRecognition captchaRecognition;
    private final WebClient webClient = new WebClient();
    private final static String apiToken = "893298284603e7a13e4e0d42140b6935";
    private long lock_t = 10_000;
    public ValueAdjuster() {
    }
    public void adjust(){
        Double value = Double.valueOf(captchaRecognition.getBalance());
        value+=0.1;

        try {
            Thread.sleep(lock_t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
