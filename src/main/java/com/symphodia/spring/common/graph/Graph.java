package com.symphodia.spring.common.graph;



import java.util.HashMap;
import java.util.Map;


public class Graph {

    private StatusNode inAnyStatus;

    private Map<Enum, StatusNode> statusNodes = new HashMap<>();

    private Graph() {
    }

    public static Graph graph(StatusNode... origins) {
        Graph graph = new Graph();
        for (StatusNode origin : origins) {
            doAnyOrigin(graph, origin);
            doStateNode(graph, origin);
        }
        
        graph.addEmptyAnyOriginIfMissing();
        
        return graph;
    }

    private void addEmptyAnyOriginIfMissing() {
        if (inAnyStatus == null) {
            inAnyStatus = StatusNode.inAnyStatus();
        }
    }

    private static void doAnyOrigin(Graph graph, StatusNode origin) {
        if (origin.getStatus() == null) {
            if (graph.inAnyStatus != null) {
                throw new IllegalArgumentException("Cannot have more than one AnyOrigin");
            }

            graph.inAnyStatus = origin;
        }
    }

    private static void doStateNode(Graph graph, StatusNode node) {
        if (graph.statusNodes.containsKey(node.getStatus())) {
            throw new IllegalArgumentException("Cannot specify Status in more than one StatusNode");
        }

        graph.statusNodes.put(node.getStatus(), node);
    }

    public Commands getCommands(Enum status, Class<? extends Event> eventClass) {
        Commands commandsFromStatus = commandsFromStatus(status, eventClass);
        if(commandsFromStatus != null) {
            return commandsFromStatus;
        }

        return inAnyStatus.getCommands(eventClass);
    }
    
    private Commands commandsFromStatus(Enum status, Class<? extends Event> eventClass) {
        StatusNode statusNode = statusNodes.get(status);

        return statusNode != null ? statusNode.getCommands(eventClass) : null;
    }

    public OnEvent getOnEvent(Enum status, Class<? extends Event> eventClass) {
        if((statusNodes.get(status) != null) && (statusNodes.get(status).getOnEvent(eventClass) != null)) {
            return statusNodes.get(status).getOnEvent(eventClass);
        }
        return inAnyStatus.getOnEvent(eventClass);
    }

    public Enum getNextSuccessStatus(Enum currentStatus, Class<? extends Event> eventClass) {
        Enum status = nextSuccessFromSpecificStatus(currentStatus, eventClass);

        return status != null ? status : nextSuccessFromAnyStatus(eventClass);
    }

    private Enum nextSuccessFromSpecificStatus(Enum currentStatus, Class<? extends Event> eventClass) {
        StatusNode origin = statusNodes.get(currentStatus);

        if (origin == null || origin.getOnEvent(eventClass) == null) {
            return null;
        }

        return origin.getOnEvent(eventClass).getNextSuccessStatus();
    }

    private Enum nextSuccessFromAnyStatus(Class<? extends Event> eventClass) {
        StatusNode origin = inAnyStatus;

        if (origin.getOnEvent(eventClass) == null) {
            throw new IllegalStateException("No 'on event' definition found for " + origin.toString() + ", " + eventClass.getSimpleName());
        }

        return origin.getOnEvent(eventClass).getNextSuccessStatus();
    }

    public Enum getNextFailureStatus(Enum currentStatus, Class<? extends Event> eventClass) {
        Enum status = nextFailureFromSpecificStatus(currentStatus, eventClass);

        return status != null ? status : nextFailureFromAnyStatus(eventClass);
    }

    private Enum nextFailureFromSpecificStatus(Enum currentStatus, Class<? extends Event> eventClass) {
        StatusNode origin = statusNodes.get(currentStatus);

        if (origin == null || origin.getOnEvent(eventClass) == null) {
            return null;
        }

        return origin.getOnEvent(eventClass).getNextFailureStatus();
    }

    private Enum nextFailureFromAnyStatus(Class<? extends Event> eventClass) {
        StatusNode origin = inAnyStatus;

        if (origin.getOnEvent(eventClass) == null) {
            throw new IllegalStateException("No 'on event' definition found for " + origin.toString() + ", " + eventClass.getSimpleName());
        }

        return origin.getOnEvent(eventClass).getNextFailureStatus();
    }
}
