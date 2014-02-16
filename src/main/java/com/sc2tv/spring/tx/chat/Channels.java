package com.sc2tv.spring.tx.chat;

import com.sc2tv.spring.tx.WebClient;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Channels {
    public List<Channel> getChannels() {
        return channels;
    }

    public List<Channel> channels = new ArrayList<>();

    private WebClient webClient = new WebClient();

    public String getTitleByStreamerName(String streamerName) {
        for (Channel channel : channels) {
            if (channel.getStreamerName().equals(streamerName))
                return channel.getChannelTitle();
        }
        return "";

    }
    public String getTitleByChannelId(String channelId) {
        for (Channel channel : channels) {
            if (channel.getChannelId().equals(channelId))
                return channel.getChannelTitle();
        }
        return "";
    }

    public String evaluateChannel(String name) {
        String url = "";
        String resp = webClient.executeGet("http://sc2tv.ru/");
        // Document doc = Jsoup.parse(resp);
        // resp = doc.select("div[id=user-stream]").toString();
        Pattern p = null;
        try {
            p = Pattern.compile(String.format("(?s)(?<=class=\"user\">%s.{0,1600}a href=\").{0,200}(?=\" class=\"hover\">Play)", name));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        Matcher matcher = p.matcher(resp);
        if (matcher.find())
            url = "http://sc2tv.ru" + matcher.group(0);
        return url;
    }

    public String getStreamerNameByChannelId(String channelId) {
        for (Channel channel : channels) {
            if (channel.getChannelId().equals(channelId))
                return channel.getStreamerName();
        }
        return "";
    }
    public String getPlayer(String channelId){
        String url = evaluateChannel(getStreamerNameByChannelId(channelId));
        String response = new WebClient().executeGet(url, new HashMap<String, String>());
        Document doc = Jsoup.parse(response);
        return(doc.select("div[id=stream_player_body]").html());
    }

    public String getChannelIdByStreamerName(String streamerName) {
        for (Channel channel : channels) {
            if (channel.getStreamerName().equals(streamerName))
                return channel.getChannelId();
        }
        return "";
    }

    public Map<String, List<Message>> getAllMessages() {
        Map<String, List<Message>> toReturn = new HashMap<>();
        for (Channel channel : channels)
            toReturn.put(channel.getChannelId(), channel.getMessages());
        return toReturn;
    }

    public Map<String, Channel> getAllChannels(){
        Map<String, Channel> toReturn = new HashMap<>();
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
        for (Object object : objects) {
            String streamerName = (String) ((JSONObject) object).get("streamerName");
            if (streamerName == null)
                continue;
            String channelTitle = ((String) ((JSONObject) object).get("channelTitle"));
            String channelId = (String) ((JSONObject) object).get("channelId");
            toReturn.put(channelId, new Channel(streamerName, channelTitle, channelId));
        }

        return toReturn;
    }
    public Channels(int i){};
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
        for (Object object : objects) {
            String streamerName = (String) ((JSONObject) object).get("streamerName");
            if (streamerName == null)
                streamerName = "";
            String channelTitle = ((String) ((JSONObject) object).get("channelTitle"));
            String channelId = (String) ((JSONObject) object).get("channelId");
            channels.add(new Channel(streamerName, channelTitle, channelId));
        }
    }

    public List<String> getAllIds() {
        List<String> toReturn = new ArrayList<>();
        for (Channel channel : channels) {
            toReturn.add(channel.getChannelId());
        }
        return toReturn;
    }
    public JSONArray getModMessages(){
        String resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channel-moderator.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("messages");
        return objects;
    }
    public void refresh() {
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
        for (Object object : objects) {
            String streamerName = (String) ((JSONObject) object).get("streamerName");
            if (streamerName == null)
                streamerName = "";
            String channelTitle = (String) ((JSONObject) object).get("channelTitle");
            String channelId = (String) ((JSONObject) object).get("channelId");
            channels.add(new Channel(streamerName, channelTitle, channelId));
        }
    }
    public List<String> getNames(){
        List<String> toReturn = new ArrayList<>();
        String resp = webClient.executeGet("http://chat.sc2tv.ru/memfs/channel-moderator.json", new HashMap<String, String>());
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray objects = (JSONArray) jsonObj.get("messages");
        for (Object object : objects) {
            String name = (String) ((JSONObject) object).get("name");
            if (name != null)
                if(!toReturn.contains(name))
                    toReturn.add(name);
        }
        return toReturn;
    }
}
