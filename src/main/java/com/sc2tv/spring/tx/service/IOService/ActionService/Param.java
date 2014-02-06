package com.sc2tv.spring.tx.service.IOService.ActionService;

public class Param {
    private String name;
    private boolean required;
    public ParamType getParamType() {
        return paramType;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    private ParamType paramType;

    public Param(String name, boolean required, ParamType paramType) {
        this.name = name;
        this.required = required;
        this.paramType = paramType;
    }
}
