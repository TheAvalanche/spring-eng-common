package com.symphodia.spring.common.graph;

import com.google.common.base.Preconditions;

public class SuccessTransition extends AbstractTransition {

    protected SuccessTransition(Enum targetStatus, Commands postCommands) {
        super(targetStatus, postCommands);
    }

    public static SuccessTransition onSuccess(Enum targetStatus) {
        Preconditions.checkNotNull(targetStatus);
        
        return new SuccessTransition(targetStatus, Commands.noCommand());
    }

    public static SuccessTransition onSuccess(Enum targetStatus, Class<? extends ProcessCommand>... postCommands) {
        Preconditions.checkNotNull(targetStatus);
        Preconditions.checkNotNull(postCommands);

        return new SuccessTransition(targetStatus, Commands.commands(postCommands));
    }

}
