package com.symphodia.spring.common.graph;

import com.google.common.base.Preconditions;


public class OnEvent {

    private Class <? extends Event> eventClass;
    
    private Commands commands;

    private SuccessTransition successTransition;

    private FailureTransition failureTransition;

    public static OnEvent onEvent(Class <? extends Event> eventClass, Commands commands, SuccessTransition successTransition) {
        return onEvent(eventClass, commands, successTransition, null);
    }

    public static OnEvent onEvent(Class <? extends Event> eventClass, Commands commands, SuccessTransition successTransition, FailureTransition failureTransition) {
        Preconditions.checkNotNull(eventClass);
        Preconditions.checkNotNull(commands);
        Preconditions.checkNotNull(successTransition);

        OnEvent onEvent = new OnEvent();
        onEvent.eventClass = eventClass;
        onEvent.commands = commands;
        onEvent.successTransition = successTransition;
        onEvent.failureTransition = failureTransition;
        return onEvent;
    }
    
    public Class <? extends Event> getEventClass() {
        return eventClass;
    }

    public Commands getCommands() {
        return commands;
    }

    public Enum getNextSuccessStatus() {
        return successTransition.getTargetStatus();
    }

    public Enum getNextFailureStatus() {
        return failureTransition.getTargetStatus();
    }

    public Enum getNextStatus(Outcome outcome, Enum currentStatus) {
        switch (outcome.getStatus()) {
            case FAILURE:
                return failureTransition.getTargetStatus();
            case SUCCESS:
                return successTransition.getTargetStatus();
            default:
                return currentStatus;
        }
    }

    public Commands getPostCommands(Outcome outcome) {
        switch (outcome.getStatus()) {
            case FAILURE:
                return failureTransition.getPostCommands();
            case SUCCESS:
                return successTransition.getPostCommands();
            default:
                return Commands.noCommand();
        }
    }

}
