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
import java.util.Random;

@Service
public class WriteInChat implements ExecutableFunction {
    @Autowired
    Channels channels;
    @Autowired
    UserManager userManager;
    @Override
    public List<Param> getRequiredParams() {
        return requiredParams;
    }
    private
    List<Param> requiredParams= new ArrayList<Param>(){};

    public WriteInChat() {
        requiredParams.add(new Param("message", true,ParamType.STRING));
        requiredParams.add(new Param("user", false, ParamType.STRINARR));
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
    }

    public String execute(Map<Param, Object> params) throws IllegalAccessException, InstantiationException {
        String message = null;
        String names = null;
        String channelId = null;
        String[] userNames;
        for(Param param: params.keySet()){
            Object o = params.get(param);
            switch(param.getName()){
                case "message":{
                    message = String.valueOf(o);
                    break;
                }
                case "user":{
                    names = String.valueOf(o);
                    break;
                }
                case "channelId":{
                    channelId = String.valueOf(o);
                    break;
                }
            }
        }
        if(channelId == null){
            return "channel not found";
        }
        if(names!=null){
            Sc2TvUser sc2TvUser;
            User user;
            userNames = names.split(",");
            for(String userName: userNames)
            if((sc2TvUser = userManager.getUser(userName))==null){
            }
            else{
                user = new User(sc2TvUser);
                user.sendMessage(message, channelId);
            }
            return "done";
        }
        else{
            User user;
            Random random = new Random();
            List<User> users = userManager.getUsers();
            user = users.get(random.nextInt(users.size()));
            user.sendMessage(message, channelId);
            return "done";
        }
    }
}

