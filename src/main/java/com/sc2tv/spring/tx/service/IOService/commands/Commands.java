package com.sc2tv.spring.tx.service.IOService.commands;

import com.sc2tv.spring.tx.service.IOService.Command;

import java.util.HashMap;

public interface Commands {
    public boolean addCommand(Command command);
    public boolean addCommand(String command, HashMap<String, String> parameters);
    public String execute(Command command);
}
