package com.sc2tv.spring.tx;

import java.util.HashMap;

public class Scanner{
    private boolean readMod = false;
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    private String chatId;
    private WebClient client = new WebClient();
    public String getResponse() {
        if(!readMod)
            return client.executeGet("http://chat.sc2tv.ru/memfs/channel-"+chatId+".json", new HashMap<String, String>());
        else
            return client.executeGet("http://chat.sc2tv.ru/memfs/channel-moderator.json");
    }

    public String response;
    public Scanner(String chatId) {
        this.chatId = chatId;
    }
    public Scanner() {
        readMod = true;
    }
}
