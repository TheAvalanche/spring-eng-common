package com.symphodia.spring.common.graph;


import com.symphodia.spring.common.log.AutowiredLogger;
import com.symphodia.spring.common.property.PropertyService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Service
public class EventProcessRegistry {

    @AutowiredLogger
    Logger logger;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PropertyService propertyService;

    private static Map<ProcessKey, EventProcess> eventProcessMap = new HashMap<>();

    public void registerAllFromPackage(String basePackage) throws Exception {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(EventProcess.class));
        scanner.findCandidateComponents(basePackage).forEach(this::addToMap);
        logger.info("Total: {} event processes found", eventProcessMap.size());
    }

    private void addToMap(BeanDefinition bd) {
        try {
            logger.info("Adding {} to process map", bd.getBeanClassName());
            EventProcess eventProcess = (EventProcess) applicationContext.getBean(Class.forName(bd.getBeanClassName()));
            eventProcessMap.put(eventProcess.getKey(), eventProcess);
        } catch (Exception e) {
            logger.error(format("Failed to add event process %s to map", bd.getBeanClassName()), e);
        }
    }

    public EventProcess lookup(ProcessableItem pi) {
        ProcessKey key = pi.getProcessKey();
        logger.debug("Looking process for: {}", pi.getId());
        EventProcess eventProcess = eventProcessMap.get(key);
        if (eventProcess == null) {
            throw new IllegalStateException(format("No event process could be found for %s with key %s", pi.getId(), key.toString()));
        }

        return eventProcess;
    }
}
