package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VoteStreamer implements ExecutableFunction {
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


    public VoteStreamer() {
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
        requiredParams.add(new Param("vote", true, ParamType.INTEGER));
        requiredParams.add(new Param("amount", true, ParamType.INTEGER));
    }

    @Override
    public String execute(Map<Param, Object> params) {
        String channelId = "";
        int vote = 0;
        int amount = 0;
        for (Param param : params.keySet()) {
            Object o = params.get(param);
            switch (param.getName()) {
                case "vote": {
                    vote = Integer.valueOf(param.objToString(o));
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
            }
        }
        int counter = 0;
        User user;
        Map<String, User> users = userManager.getAvaliableUsers();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final String streamerUrl = channels.evaluateChannel(channels.getStreamerNameByChannelId(channelId));
        if (vote == 1) {
            for (final User usr : users.values()) {
                try {
                    if (usr.isLoggedIn())
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                usr.voteUp(streamerUrl);
                            }
                        });
                    else {
                    }
                } catch (Exception exp) {
                }
                counter++;
            }
        } else if (vote == -1) {
            for (final User usr : users.values()) {
                try {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            usr.voteDown(streamerUrl);
                        }
                    });
                } catch (Exception exp) {
                }
                counter++;
            }
        }
        executorService.shutdown();
        return "done";
    }
}
