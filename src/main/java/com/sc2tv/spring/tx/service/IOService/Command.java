package com.sc2tv.spring.tx.service.IOService;

import java.util.Map;

public class Command {
    String time;
    String command;
    Map<String, String> objectMap;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, String> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, String> objectMap) {
        this.objectMap = objectMap;
    }

    public Command(String time, String command, Map<String, String> objectMap) {
        this.time = time;
        this.command = command;
        this.objectMap = objectMap;
    }
}
