package com.sc2tv.spring.tx.service.IOService.ActionService.Functions;

import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.ScanChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.VoteRedChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.VoteStreamer;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.WriteInChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Functions {
    public Functions() {

    }

    @PostConstruct
    public void init() {
        functionMap = new HashMap<>();
        try {
            functionMap.put("scanchat", ScanChat.class.newInstance());
            functionMap.put("voteredchat", VoteRedChat.class.newInstance());
            functionMap.put("votestreamer", VoteStreamer.class.newInstance());
            functionMap.put("write", WriteInChat.class.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ExecutableFunction> getFunctionMap() {
        return functionMap;
    }

    String functionNames;

    public String getFunctionNames() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String key : functionMap.keySet()) {
            builder.append(String.format("\"%s\": [", key));
            int i = 0;
            List<Param> params = functionMap.get(key).getRequiredParams();
            String[] array = new String[params.size()];
            for (Param param : params) {
                array[i++] = String.format("\"-%s\"", param.getName());
            }
            String temp = StringUtils.join(array, ", ") + "]";
            builder.append(temp);
            builder.append(", ");
        }
        builder.replace(builder.lastIndexOf(", "), builder.lastIndexOf(", ") + 2, "");
        String toReturn = builder.append("}").toString();
        return toReturn;
    }

    String parameters;

    public String getParameters(String functionName) {
        String toReturn;
        List<String> pNames = new ArrayList<>();
        for (Param p : functionMap.get(functionName).getRequiredParams()) {
            pNames.add(String.format("\"%s\"", p.getName()));
        }
        toReturn = StringUtils.join(pNames, ", ");
        return toReturn;
    }

    public Map<String, ExecutableFunction> functionMap;

}

