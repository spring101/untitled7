package com.sc2tv.spring.tx.service.IOService.ActionService.Factory;

import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;

import java.util.Map;

public interface FunctionFactory {
    public String execute(String functionName, Map<Param, Object> params) throws InstantiationException, IllegalAccessException;
    public ExecutableFunction FUNCTION(String functionName);
}
