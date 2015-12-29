package com.symphodia.spring.common.graph;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

public class StatusNode {

    private Enum status;
    protected List<OnEvent> onEvents;

    private StatusNode(List<OnEvent> onEvents) {
        Preconditions.checkNotNull(onEvents);
        this.onEvents = onEvents;
    }

    public static StatusNode statusNode(Enum status, OnEvent... onEvents) {
        Preconditions.checkNotNull(status);
        StatusNode node = new StatusNode(Arrays.asList(onEvents));
        node.status = status;
        return node;
    }

    public static StatusNode inAnyStatus(OnEvent... onEvents) {
        StatusNode node = new StatusNode(Arrays.asList(onEvents));
        node.status = null;
        return node;
    }

    public Enum getStatus() {
        return status;
    }

    public OnEvent getOnEvent(Class<? extends Event> eventClass) {
        for (OnEvent onEvent : onEvents) {
            if (onEvent.getEventClass().equals(eventClass)) {
                return onEvent;
            }
        }

        return null;
    }

    public Commands getCommands(Class<? extends Event> eventClass) {
        Preconditions.checkNotNull(eventClass);

        for (OnEvent onEvent : onEvents) {
            if (onEvent.getEventClass().equals(eventClass)) {
                return onEvent.getCommands();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
