package com.symphodia.spring.common.graph;

public interface MessageProcess<T extends Message> {

    ProcessKey getKey();

    Outcome process(T message);

}
