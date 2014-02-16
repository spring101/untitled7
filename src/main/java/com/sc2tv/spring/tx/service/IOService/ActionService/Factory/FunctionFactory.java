package com.sc2tv.spring.tx.service.IOService.ActionService.Factory;

import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;

public interface FunctionFactory {
    public ExecutableFunction FUNCTION(String functionName);
}
