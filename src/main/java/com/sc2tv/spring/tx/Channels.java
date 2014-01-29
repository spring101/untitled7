package com.sc2tv.spring.tx;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Channels {
    public List<Channel> getChannels() {
        return channels;
    }

    public List<Channel> channels = new ArrayList<>();

    private WebClient webClient = new WebClient();
    public String getTitleByStreamerName(String streamerName){
        for(Channel channel: channels){
            if(channel.getStreamerName().equals(streamerName))
                return channel.getChannelTitle();
        }
        return "";
    };
    public String getTitleByChannelId(String channelId){
        for(Channel channel: channels){
            if(channel.getChannelId().equals(channelId))
                return channel.getChannelTitle();
        }
        return "";
    };
    public String getStreamerNameByChannelId(String channelId){
        for(Channel channel: channels){
            if(channel.getChannelId().equals(channelId))
                return channel.getStreamerName();
        }
        return "";
    };
    public String getChannelIdByStreamerName(String streamerName){
        for(Channel channel: channels){
            if(channel.getStreamerName().equals(streamerName))
                return channel.getChannelId();
        }
        return "";
    }

    public class Channel{
        private String streamerName;
        private String channelTitle;
        private String channelId;
        public String getChannelId() {
            return channelId;
        }
        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
        public String getStreamerName() {
            return streamerName;
        }
        public void setStreamerName(String streamerName) {
            this.streamerName = streamerName;
        }
        public String getChannelTitle() {
            return channelTitle;
        }
        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }
        public Channel(String streamerName, String channelTitle, String channelId) {
            this.streamerName = streamerName;
            this.channelTitle = channelTitle;
            this.channelId = channelId;
        }
    }
    public Channels() {
        String resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channels.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("channel");
        objects.remove(0);
        for(Object object: objects){
            String streamerName = (String) ((JSONObject) object).get("streamerName");
            String channelTitle = ((String) ((JSONObject) object).get("channelTitle"));
            String channelId = (String) ((JSONObject) object).get("channelId");
            channels.add(new Channel(streamerName, channelTitle, channelId));
        }
       // autoRefresh();
    }
    public List<String> getAllIds(){
        List<String> toReturn = new ArrayList<>();
        for(Channel channel: channels){
            toReturn.add(channel.getChannelId());
        }
        return toReturn;
    }
    public void refresh(){
        channels.clear();
        String resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channels.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("channel");
        objects.remove(0);
        for(Object object: objects){
            String streamerName = (String) ((JSONObject) object).get("streamerName");
            String channelTitle = (String) ((JSONObject) object).get("channelTitle");
            String channelId = (String) ((JSONObject) object).get("channelId");
            channels.add(new Channel(streamerName, channelTitle, channelId));
        }
    }
    private void autoRefresh(){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    refresh();
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
