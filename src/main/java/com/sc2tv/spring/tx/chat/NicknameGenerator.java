package com.sc2tv.spring.tx.chat;

import java.util.HashMap;
import java.util.List;

public class NicknameGenerator {
    HashMap<String,String> replacements = null;
    public NicknameGenerator() {
        replacements = new HashMap<String,String>();
        replacements.put("a", "а");
        replacements.put("o", "о");
        replacements.put("p", "р");
        replacements.put("c", "с");
        replacements.put("x", "х");
        replacements.put("H", "Н");
        replacements.put("M", "М");
        replacements.put("K", "К");
    }
    private List<String> getNames(long time){
        Channels channels = new Channels();
        List<String> names = channels.getNames();
        Thread t = new Thread();
        try {
            t.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(t.getState() == Thread.State.TIMED_WAITING){
            List<String> toAdd = channels.getNames();
            for(String s: toAdd){
                if(!names.contains(s))
                    names.add(s);
            }
        };
        return names;
    }
    public String[] generate(long time){
        List<String> getNames =  getNames(time);
        String names = "";
        for(String s: getNames){
            names += s + "\t";
        }
        for(String key: replacements.keySet()){
            names = names.replace(key, replacements.get(key));
            names = names.replace(key.toUpperCase(), replacements.get(key).toUpperCase());
        }
        return names.split("\t");
    }
}
