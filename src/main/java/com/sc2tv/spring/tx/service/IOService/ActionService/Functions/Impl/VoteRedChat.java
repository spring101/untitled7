package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VoteRedChat implements ExecutableFunction {
    @Autowired
    Channels channels;
    @Autowired
    UserManager userManager;

    @Override
    public List<Param> getRequiredParams() {
        return requiredParams;
    }

    List<Param> requiredParams = new ArrayList<Param>() {
    };


    public VoteRedChat() {
        requiredParams.add(new Param("options", true, ParamType.STRING));
        requiredParams.add(new Param("amount", false, ParamType.INTEGER));
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
        requiredParams.add(new Param("delay", false, ParamType.INTEGER));
    }

    @Override

    public String execute(Map<Param, Object> params) {
        String options = "", channelId = "";
        int amount = 0, delay = 1000;

        for (Param param : params.keySet()) {
            Object o = params.get(param);
            switch (param.getName()) {
                case "options": {
                    options = param.objToString(o) + ".";
                    break;
                }
                case "channelId": {
                    channelId = param.objToString(o);
                    break;
                }
                case "amount": {
                    amount = Integer.valueOf(param.objToString(o));
                    break;
                }
                case "delay": {
                    delay = Integer.valueOf(param.objToString(o));
                    break;
                }
            }
        }
        long seed = System.nanoTime();
        Map<String, User> users = userManager.getAvaliableUsers();
        //Collections.shuffle(users, new Random(seed));
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int counter = 10;
        for (final User user : users.values()) {
            if(counter--==0){
                executorService.shutdown();
                return "done";
            }
            final String finalChannelId = channelId;
            final String finalOptions = options;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    user.sendMessage(finalOptions, finalChannelId);
                }
            });
        }
        executorService.shutdown();
        return "done";
    }
}
