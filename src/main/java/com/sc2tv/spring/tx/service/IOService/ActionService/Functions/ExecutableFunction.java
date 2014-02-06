package com.sc2tv.spring.tx.service.IOService.ActionService.Functions;

import com.sc2tv.spring.tx.service.IOService.ActionService.Param;

import java.util.List;
import java.util.Map;
public interface ExecutableFunction {
    public List<Param> getRequiredParams();
    public String execute(Map<Param, Object> params) throws IllegalAccessException, InstantiationException;
}
