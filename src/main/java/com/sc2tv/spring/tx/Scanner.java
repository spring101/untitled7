package com.sc2tv.spring.tx;

import java.util.HashMap;

public class Scanner{
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    private String chatId;
    private WebClient client = new WebClient();
    public String getResponse() {
        return client.executeGet("http://chat.sc2tv.ru/memfs/channel-"+chatId+".json", new HashMap<String, String>());
    }

    public String response;
    public Scanner(String chatId) {
        this.chatId = chatId;
    }
}
