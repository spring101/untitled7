package com.sc2tv.spring.tx.service.IOService.ActionService.Factory.Impl;


import com.sc2tv.spring.tx.service.IOService.ActionService.Factory.FunctionFactory;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Functions;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FunctionFactoryImpl implements FunctionFactory {
    @Autowired
    Validator validator;
    @Autowired
    Functions functions;
    @Override
    public String execute(String functionName, Map<Param, Object> params) throws InstantiationException, IllegalAccessException {
        return functions.functionMap.get(functionName).execute(params);
    }

    @Override
    public ExecutableFunction FUNCTION(String functionName) {
        return functions.functionMap.get(functionName);
    }
}
