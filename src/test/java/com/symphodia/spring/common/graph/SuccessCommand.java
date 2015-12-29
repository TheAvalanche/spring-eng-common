package com.symphodia.spring.common.graph;


public class SuccessCommand implements ProcessCommand {
    @Override
    public Outcome invoke(ProcessableItem processableItem, Event messageEvent) {
        return Outcome.success();
    }
}
