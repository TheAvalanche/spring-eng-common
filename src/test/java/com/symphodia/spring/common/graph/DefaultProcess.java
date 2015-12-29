package com.symphodia.spring.common.graph;

import static com.symphodia.spring.common.graph.Commands.commands;
import static com.symphodia.spring.common.graph.DefaultStatus.*;
import static com.symphodia.spring.common.graph.FailureTransition.onFailure;
import static com.symphodia.spring.common.graph.Graph.graph;
import static com.symphodia.spring.common.graph.OnEvent.onEvent;
import static com.symphodia.spring.common.graph.StatusNode.inAnyStatus;
import static com.symphodia.spring.common.graph.StatusNode.statusNode;
import static com.symphodia.spring.common.graph.SuccessTransition.onSuccess;

public class DefaultProcess implements EventProcess {


    @Override
    public ProcessKey getKey() {
        return new DefaultProcessKey("Test");
    }

    public Graph getGraph() {
        return graph(
                statusNode(
                        NEW,
                        onEvent(TestEvent1.class,
                                commands(SuccessCommand.class),
                                onSuccess(VALIDATED, SuccessCommand.class),
                                onFailure(FAILED_VALIDATION, FailureCommand.class)),
                        onEvent(TestEvent2.class,
                                commands(SuccessCommand.class),
                                onSuccess(ACTIVATED))),
                inAnyStatus(
                        onEvent(TestEvent3.class,
                                commands(FailureCommand.class),
                                onSuccess(PLACED),
                                onFailure(FAILED_SUBSCRIPTION_CREATION))
                )
        );
    }

}
