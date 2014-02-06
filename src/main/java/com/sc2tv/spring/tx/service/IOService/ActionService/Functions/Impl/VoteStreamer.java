package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    List<Param> requiredParams = new ArrayList<Param>(){};


    public VoteStreamer() {
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
        requiredParams.add(new Param("vote", true, ParamType.INTEGER));
        requiredParams.add(new Param("amount", true, ParamType.INTEGER));
    }

    @Override
    public String execute(Map<Param, Object> params) {
        String channelId = null;
        int vote = 0;
        int amount = 0;
        for(Param param: params.keySet()){
            Object o = params.get(param);
            switch(param.getName()){
                case "vote":{
                    vote = Integer.valueOf(String.valueOf(o));
                    break;
                }
                case "channelId":{
                    channelId = channels.evaluateChannel(String.valueOf(o));
                    break;
                }
                case "amount":{
                    amount = Integer.valueOf(String.valueOf(o));
                    break;
                }
            }
            int counter = 0;
            User user;
            if(vote==1){
                for(Sc2TvUser sc2TvUser: userManager.getNotBanned()){
                    if(counter>amount){
                        break;
                    }
                    user = new User(sc2TvUser);
                    user.voteUp(channelId);
                    counter++;
                }
            }
            else if(vote==-1){
                for(Sc2TvUser sc2TvUser: userManager.getNotBanned()){
                    if(counter>amount){
                        break;
                    }
                    user = new User(sc2TvUser);
                    user.voteDown(channelId);
                    counter++;
                }
            }
        }
        return "done";
    }
}
