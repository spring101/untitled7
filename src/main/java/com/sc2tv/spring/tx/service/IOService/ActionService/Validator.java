package com.sc2tv.spring.tx.service.IOService.ActionService;

import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class Validator {
    Map<String, String> params;
        Map<String, List<Param>> requiredParams;
    {
        requiredParams = new HashMap<String, List<Param>>();
        requiredParams.put("scan", new ArrayList<Param>(){{
            new Param("options",true,ParamType.INTARRAY);
            new Param("threads", false, ParamType.INTEGER);
            new Param("channelId", true, ParamType.STRING);
            new Param("money", false, ParamType.BOOL);
        }});
        requiredParams.put("redChat", new ArrayList<Param>(){{
            new Param("options",true,ParamType.INTARRAY);
            new Param("amount", true, ParamType.INTEGER);
            new Param("channelId", true, ParamType.STRING);
            new Param("delay", false, ParamType.INTARRAY);
        }});
        requiredParams.put("write", new ArrayList<Param>(){{
            new Param("message", true,ParamType.STRING);
            new Param("user", false, ParamType.STRING);
            new Param("channelId", true, ParamType.STRING);
        }});
        requiredParams.put("vote", new ArrayList<Param>(){{
            new Param("channelId", true, ParamType.STRING);
            new Param("vote", true, ParamType.INTEGER);
        }});
    }
    public Validator() {
    }
    public void validateAndEvaluate(Map<Param, Object> params, List<Param> paramTypes) throws Exception {
        evaluateVariables(params);
    }
    public void validate(Map<String, String> params, ExecutableFunction executableFunction) throws Exception {
        for(Param param: executableFunction.getRequiredParams()){
            if(params.get(param.getName())==null&&param.isRequired()){
                throw new Exception("Parameter not specified: " + param.getName());
            }
        }
    }
    public Map<Param, Object> convertParams(Map<String, String> paramsToConvert, ExecutableFunction executableFunction){
        Map<Param, Object> toReturn = new HashMap<>();
        for(Param param: executableFunction.getRequiredParams()){
            if(paramsToConvert.get(param.getName())!=null){
               toReturn.put(param, paramsToConvert.get(param.getName()));
            }
        }
        return toReturn;
    }
    public void proc(ExecutableFunction executableFunction) throws Exception {
        List<Param> reqP;
        if((reqP=requiredParams.get(executableFunction.getRequiredParams()))==null){
            throw new Exception("Function not implemented");
        }
        else{
            //validateAndEvaluate(convertParams(executableFunction.), reqP);
        }
    }
    public void evaluateVariables(Map<Param, Object> params) throws Exception {
        for(Param param: params.keySet()){
            switch (param.getParamType()){
                case INTEGER:{
                    params.put(param, Integer.parseInt((String)params.get(param)));
                }
                case BOOL:{
                    params.put(param, Boolean.parseBoolean((String) params.get(param)));
                }
                case INTARRAY:{
                    String[] tempArr = ((String)params.get(param)).split(",");
                    int lenght=0;
                    if((lenght = tempArr.length)==0)
                        throw new Exception("Param null size");
                    int[] toReturn = new int[lenght];
                    for(int i = 0; i < lenght; i++){
                        toReturn[i] = Integer.parseInt(tempArr[i]);
                    }
                    params.put(param, toReturn);
                }
                case STRING:{
                    params.put(param, (String) params.get(param));
                }
            }
        }
    }
    public int getInt(String param) throws NumberFormatException{
        return Integer.parseInt(params.get(param));
    }
    public long getLong(String param)throws NumberFormatException{
        return Long.parseLong(params.get(param));
    }
    public float getFloat(String param)throws NumberFormatException{
        return Float.parseFloat(params.get(param));
    }
    public <T extends Object> T getValue(String param, Class<T> type) throws ClassCastException{
        return type.cast(params.get(param));
    }
    public int[] getIntArray(String param, String splitter){
        String[] tempArr = params.get("param").split(splitter);
        int lenght=0;
        if((lenght = tempArr.length)==0)
            return null;
        int[] toReturn = new int[lenght];
        for(int i = 0; i < lenght; i++){
            toReturn[i] = Integer.parseInt(tempArr[i]);
        }
        return toReturn;
    }
}
