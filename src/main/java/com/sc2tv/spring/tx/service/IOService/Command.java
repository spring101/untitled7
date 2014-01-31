package com.sc2tv.spring.tx.service.IOService;

public class Command {
    String time;
    String command;


    public Command(String time, String command) {
        this.time = time;
        this.command = command;
    }
    public Command(){

    };

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
}
