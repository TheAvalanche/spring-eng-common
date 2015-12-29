package com.symphodia.spring.common.graph;


import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;

public class FailureCommand implements ProcessCommand {
    @Override
    public Outcome invoke(ProcessableItem processableItem, Event messageEvent) {
        return Outcome.failure(new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Some error"));
    }
}
