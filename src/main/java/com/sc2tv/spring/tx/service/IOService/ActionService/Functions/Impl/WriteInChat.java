package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WriteInChat implements ExecutableFunction {
    @Autowired
    private Sc2TvUserDAO sc2TvUserDAO;
    @Autowired
    ProxyManager proxyManager;
    @Autowired
    UserManager userManager;

    @Override
    public List<Param> getRequiredParams() {
        return requiredParams;
    }

    private
    List<Param> requiredParams;

    public WriteInChat() {
        requiredParams = new ArrayList<>();
        requiredParams.add(new Param("message", true, ParamType.STRING));
        requiredParams.add(new Param("user", false, ParamType.STRINARR));
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
    }

    public String execute(Map<Param, Object> params) {
        String message = null;
        String names = null;
        String channelId = null;
        String[] userNames;
        for (Param param : params.keySet()) {
            Object o = params.get(param);
            switch (param.getName()) {
                case "message": {
                    message = param.objToString(o);
                    break;
                }
                case "user": {
                    names = param.objToString(o);
                    break;
                }
                case "channelId": {
                    channelId = param.objToString(o);
                    break;
                }
            }
        }
        if (channelId == null) {
            return "channel not found";
        }
        if (names != null) {
            Sc2TvUser sc2TvUser;
            User user;
            userNames = names.split(",");
            for (String userName : userNames)
                if ((sc2TvUser = sc2TvUserDAO.getSc2TvUserByName(userName)) == null) {
                } else {
                    user = new User(sc2TvUser);
                    user.logIn();
                    user.sendMessage(message, channelId);
                }
            return "done";
        } else {

            User user = userManager.getAvaliableUser();
            if (user.isLoggedIn())
                user.sendMessage(message, channelId);
            return "done";
        }
    }
}

