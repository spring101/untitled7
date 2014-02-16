package com.sc2tv.spring.tx.service.IOService.ActionService;

public class Param {
    private String name;
    private boolean required;

    public Param() {
    }

    public ParamType getParamType() {
        return paramType;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public static String objToString(Object values) {
        StringBuilder sb = new StringBuilder();
        sb.append(values.toString());
        String str = sb.toString();
        if(str.indexOf("[")==0&&sb.lastIndexOf("]")==sb.length()-1)
        return str.substring(1, sb.toString().length() - 1);
        else return str;

    }

    private ParamType paramType;

    public Param(String name, boolean required, ParamType paramType) {
        this.name = name;
        this.required = required;
        this.paramType = paramType;
    }
}
