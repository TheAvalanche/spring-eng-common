package com.symphodia.spring.common.graph;

public interface ProcessCommand<PI extends ProcessableItem, E extends Event> {
    Outcome invoke(PI processableItem, E event);
}
