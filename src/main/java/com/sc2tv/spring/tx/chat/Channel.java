package com.sc2tv.spring.tx.chat;

import com.sc2tv.spring.tx.WebClient;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel{
    private String streamerName;
    private String channelTitle;
    private String channelId;
    public Channel(String channelId){
        String resp = new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channels.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("channel");
        boolean isFound = false;
        for(Object object: objects){
            if(((String)((JSONObject)object).get("channelId")).equals(channelId)){
                this.streamerName = (String) ((JSONObject) object).get("streamerName");
                if(this.streamerName == null)
                    this.streamerName = "";
                this.channelTitle = (String) ((JSONObject)object).get("channelTitle");
                this.channelId = channelId;
                isFound = true;
                break;
            }
        }
        if(!isFound){
            this.streamerName = "notFound";
            this.channelTitle = "notFound";
            this.channelId = "notFound";
        }
    }
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
    private String getChatResponse(){
        return new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channel-"+channelId+".json", new HashMap<String, String>());
    }
    public List<Message> getMessages(){
        String response = getChatResponse();
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(response);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Message> toReturn = new ArrayList<>();
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("messages");
        for(Object object: objects){
            Message toAdd = new Message();
            toAdd.setName((String) ((JSONObject) object).get("name"));
            toAdd.setChannelId((String) ((JSONObject) object).get("channelId"));
            toAdd.setDate((String) ((JSONObject) object).get("date"));
            toAdd.setId((String) ((JSONObject) object).get("id"));
            toAdd.setMessage((String) ((JSONObject) object).get("message"));
            toAdd.setRole((String) ((JSONObject) object).get("role"));
            toAdd.setUid((String)((JSONObject)object).get("uid"));
            toReturn.add(toAdd);
        }
        return toReturn;
    }
}