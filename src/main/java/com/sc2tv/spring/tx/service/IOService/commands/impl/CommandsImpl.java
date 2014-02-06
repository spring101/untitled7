package com.sc2tv.spring.tx.service.IOService.commands.impl;

import com.sc2tv.spring.tx.service.IOService.ActionService.Factory.Impl.FunctionFactoryImpl;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.Validator;
import com.sc2tv.spring.tx.service.IOService.Command;
import com.sc2tv.spring.tx.service.IOService.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommandsImpl implements Commands {
    @Autowired
    FunctionFactoryImpl functionFactory;
    @Autowired
    Validator validator;
    List<Command> commandList = new ArrayList<>();

    public boolean addCommand(Command command){
        if(commandList.contains(command)){
            return false;
        }
        else{
            boolean error = true;
            execute(command);
            if(error){
                commandList.add(command);
            }
            return error;
        }
    }
    public boolean addCommand(String command, HashMap<String, String> parameters){
        Command toAdd = new Command(new Date().toString(),command, parameters);
        if(commandList.contains(command)){
            return false;
        }
        else{
            boolean error = true;
            execute(toAdd);
            if(error){
                commandList.add(toAdd);
            }
            return error;
        }
    }
    public String execute(Command command) {
        try {
            validator.validate(command.getObjectMap(), functionFactory.FUNCTION(command.getCommand()));
        } catch (Exception e) {
            return e.getMessage();
        }
        Map<Param, Object> paramObjectMap = validator.convertParams(command.getObjectMap(), functionFactory.FUNCTION(command.getCommand()));
        try {
            return functionFactory.execute(command.getCommand(), paramObjectMap);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "executing";
    }
}
