package com.symphodia.spring.common.graph

import spock.lang.Specification
import static com.symphodia.spring.common.graph.DefaultStatus.*;
import static com.symphodia.spring.common.graph.Graph.*;
import static com.symphodia.spring.common.graph.StatusNode.*;
import static com.symphodia.spring.common.graph.OnEvent.*;
import static com.symphodia.spring.common.graph.SuccessTransition.*;
import static com.symphodia.spring.common.graph.FailureTransition.*;
import static com.symphodia.spring.common.graph.Commands.*;


class GraphTest extends Specification {

    def "test new status"() {
        when:
        Graph someGraph = new DefaultProcess().getGraph()
        Commands commands = someGraph.getCommands(NEW, TestEvent1.class)
        DefaultStatus successStatus = (DefaultStatus) someGraph.getNextSuccessStatus(NEW, TestEvent1.class)
        DefaultStatus failedStatus = (DefaultStatus) someGraph.getNextFailureStatus(NEW, TestEvent1.class)
        OnEvent onEvent = someGraph.getOnEvent(NEW, TestEvent1.class)
        then:
        commands.list.size() == 1
        commands.list.get(0) == SuccessCommand.class
        successStatus == VALIDATED
        failedStatus == FAILED_VALIDATION
        onEvent.commands == commands
        onEvent.nextSuccessStatus == successStatus
        onEvent.getNextStatus(Outcome.success(), null) == successStatus
        onEvent.getPostCommands(Outcome.success()).list.size() == 1
        onEvent.getPostCommands(Outcome.success()).list.get(0) == SuccessCommand.class
        onEvent.getPostCommands(Outcome.failure(new Exception())).list.size() == 1
        onEvent.getPostCommands(Outcome.failure(new Exception())).list.get(0) == FailureCommand.class
        onEvent.nextFailureStatus == failedStatus
        onEvent.getNextStatus(Outcome.failure(new Exception()), null) == failedStatus
    }

    def "test any status"() {
        when:
        Graph someGraph = new DefaultProcess().getGraph()
        Commands commands = someGraph.getCommands(VALIDATED, TestEvent3.class)
        DefaultStatus successStatus = (DefaultStatus) someGraph.getNextSuccessStatus(VALIDATED, TestEvent3.class)
        DefaultStatus failedStatus = (DefaultStatus) someGraph.getNextFailureStatus(VALIDATED, TestEvent3.class)
        OnEvent onEvent = someGraph.getOnEvent(VALIDATED, TestEvent3.class)
        then:
        commands.list.size() == 1
        commands.list.get(0) == FailureCommand.class
        successStatus == PLACED
        failedStatus == FAILED_SUBSCRIPTION_CREATION
        commands == onEvent.getCommands()
        successStatus == onEvent.nextSuccessStatus
        onEvent.getNextStatus(Outcome.success(), null) == successStatus
        onEvent.nextFailureStatus == failedStatus
        onEvent.getNextStatus(Outcome.failure(new Exception()), null) == failedStatus
        0 == onEvent.getPostCommands(Outcome.success()).list.size()
        onEvent.getPostCommands(Outcome.failure(new Exception())).list.size() == 0
    }

    def "test any status success transition failure"() {
        when:
        Graph someGraph = new DefaultProcess().getGraph()
        someGraph.getNextSuccessStatus(VALIDATED, TestEvent1.class)
        then:
        thrown(IllegalStateException)

    }

    def "test any status failure transition failure"() {
        when:
        Graph someGraph = new DefaultProcess().getGraph()
        someGraph.getNextFailureStatus(VALIDATED, TestEvent1.class)
        then:
        thrown(IllegalStateException)
    }

    def "test duplicate status node creation"() {
        when:
        graph(
                statusNode(NEW, onEvent(TestEvent1.class, noCommand(), onSuccess(VALIDATED), onFailure(FAILED_VALIDATION))),
                statusNode(NEW, onEvent(TestEvent1.class, noCommand(), onSuccess(VALIDATED), onFailure(FAILED_VALIDATION)))
        )
        then:
        thrown(IllegalArgumentException)
    }

    def "test duplicate any status node creation"() {
        when:
        graph(
                statusNode(NEW, onEvent(TestEvent1.class, noCommand(), onSuccess(VALIDATED), onFailure(FAILED_VALIDATION))),
                inAnyStatus(onEvent(TestEvent1.class, noCommand(), onSuccess(VALIDATED), onFailure(FAILED_VALIDATION))),
                inAnyStatus(onEvent(TestEvent1.class, noCommand(), onSuccess(VALIDATED), onFailure(FAILED_VALIDATION)))
        )
        then:
        thrown(IllegalArgumentException)
    }

}
