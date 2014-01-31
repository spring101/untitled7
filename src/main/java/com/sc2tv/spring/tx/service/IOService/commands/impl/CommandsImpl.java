package com.sc2tv.spring.tx.service.IOService.commands.impl;

import com.sc2tv.spring.tx.service.IOService.Command;
import com.sc2tv.spring.tx.service.IOService.commands.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CommandsImpl implements Commands {
    List<Command> commandList = new ArrayList<>();
    Map<String, Callable<Object>> functionList = new HashMap<>();
    public boolean addCommand(Command command){
        if(commandList.contains(command)){
            return false;
        }
        else{
            commandList.add(command);
            return true;
        }
    }
    public void execute(Command command){

    }
}
