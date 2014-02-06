package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

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

    List<Param> requiredParams = new ArrayList<Param>(){};


    public VoteRedChat() {
        requiredParams.add(new Param("options",true,ParamType.INTARRAY));
        requiredParams.add(new Param("amount", true, ParamType.INTEGER));
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
        requiredParams.add(new Param("delay", false, ParamType.INTARRAY));
    }

    @Override

    public String execute(Map<Param, Object> params) {
        return "error";
    }
}
