package com.symphodia.spring.common.graph;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Commands {

    private List<Class<? extends ProcessCommand>> list;

    private Commands() {
        this.list = Collections.emptyList();
    }

    @SafeVarargs
    private Commands(Class<? extends ProcessCommand>... commands) {
        this.list = Arrays.asList(commands);
    }

    @SafeVarargs
    public static Commands commands(Class<? extends ProcessCommand>... commands) {
        return new Commands(commands);
    }

    public static Commands noCommand() {
        return new Commands();
    }

    public List<Class<? extends ProcessCommand>> getList() {
        return list;
    }
    
}
