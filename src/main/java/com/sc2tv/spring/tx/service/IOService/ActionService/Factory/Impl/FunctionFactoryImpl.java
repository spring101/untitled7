package com.sc2tv.spring.tx.service.IOService.ActionService.Factory.Impl;


import com.sc2tv.spring.tx.service.IOService.ActionService.Factory.FunctionFactory;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.ExecutableFunction;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.ScanChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.VoteRedChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.VoteStreamer;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.WriteInChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionFactoryImpl implements FunctionFactory {
    @Autowired
    WriteInChat writeInChat;
    @Autowired
    VoteRedChat voteRedChat;
    @Autowired
    VoteStreamer voteStreamer;
    @Autowired
    ScanChat scanChat;

    @Override
    public ExecutableFunction FUNCTION(String functionName) {
        switch (functionName) {
            case ("write"): {
                return writeInChat;
            }
            case ("vote"): {
                return voteStreamer;
            }
            case ("scan"):
                return scanChat;

            case ("redchat"): {
                return voteRedChat;
            }
        }
        return null;
    }
   /* @Override
    public ExecutableFunction FUNCTION(String functionName) {
        try {
            return functions.functionMap.get(functionName).getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
