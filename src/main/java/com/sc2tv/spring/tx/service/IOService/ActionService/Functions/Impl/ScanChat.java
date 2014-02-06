package com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl;

import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.service.strawpool.Strawpool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class ScanChat implements ExecutableFunction {
    @Autowired
    Channels channels;
    @Autowired
    Strawpool strawpool;
    @Override
    public List<Param> getRequiredParams() {
        return requiredParams;
    }

    List<Param> requiredParams = new ArrayList<Param>(){};


    public ScanChat() {
        requiredParams.add(new Param("target",true, ParamType.STRING));
        requiredParams.add(new Param("options",true, ParamType.INTARRAY));
        requiredParams.add(new Param("threads", false, ParamType.INTEGER));
        requiredParams.add(new Param("channelId", true, ParamType.STRING));
        requiredParams.add(new Param("money", false, ParamType.BOOL));
    }

    @Override
    public String execute(Map<Param, Object> params) throws IllegalAccessException, InstantiationException {
        int[] options = new int[0];
        int threads = 0;
        String channelId = null;
        boolean money = false;
        String target = null;
        for(Param param: params.keySet()){
            Object o = params.get(param);
            switch(param.getName()){
                case "target":{
                    target = (String)o;
                    break;
                }
                case "options":{
                    options = (int[])o;
                    break;
                }
                case "threads":{
                    threads = Integer.valueOf(String.valueOf(o));
                    break;
                }
                case "channelId":{
                    channelId = channels.evaluateChannel(String.valueOf(o));
                    break;
                }
                case "money":{
                    money = Boolean.valueOf(String.valueOf(o));
                    break;
                }
            }
        }
        if(channelId == null){
            return "channel not found";
        }
        if(target.equals("all")){
            strawpool.scanAll(options, threads, money);
        }
        else if(target.equals("single")){
            strawpool.scanSingle(channelId, threads, options, money);
        }
        return "done";
    }
}
