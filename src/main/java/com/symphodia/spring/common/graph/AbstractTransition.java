package com.symphodia.spring.common.graph;


import com.google.common.base.Preconditions;

public abstract class AbstractTransition {

    protected Enum targetStatus;

    protected Commands postCommands;

    protected AbstractTransition(Enum targetStatus, Commands postCommands) {
        Preconditions.checkNotNull(targetStatus);
        Preconditions.checkNotNull(postCommands);
        this.targetStatus = targetStatus;
        this.postCommands = postCommands;
    }

    public Enum getTargetStatus() {
        return targetStatus;
    }

    public Commands getPostCommands() {
        return postCommands;
    }
}
