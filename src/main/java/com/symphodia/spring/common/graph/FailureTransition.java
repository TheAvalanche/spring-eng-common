package com.symphodia.spring.common.graph;


import com.google.common.base.Preconditions;


public class FailureTransition extends AbstractTransition {

    private FailureTransition(Enum targetStatus, Commands postCommands) {
        super(targetStatus, postCommands);
    }

    public static FailureTransition onFailure(Enum targetStatus) {
        Preconditions.checkNotNull(targetStatus);
        
        return new FailureTransition(targetStatus, Commands.noCommand());
    }

    public static FailureTransition onFailure(Enum targetStatus, Class<? extends ProcessCommand>... postCommands) {
        Preconditions.checkNotNull(targetStatus);
        Preconditions.checkNotNull(postCommands);

        return new FailureTransition(targetStatus, Commands.commands(postCommands));
    }

}
