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
public class MessageProcessRegistry {

    @AutowiredLogger
    Logger logger;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PropertyService propertyService;

    private static Map<ProcessKey, MessageProcess> orderProcessMap = new HashMap<>();

    public void registerAllFromPackage(String basePackage) throws Exception {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(MessageProcess.class));
        scanner.findCandidateComponents(basePackage).forEach(this::addToMap);
        logger.info("Total: {} order processes found", orderProcessMap.size());
    }

    private void addToMap(BeanDefinition bd) {
        try {
            logger.info("Adding {} to process map", bd.getBeanClassName());
            MessageProcess messageProcess = (MessageProcess) applicationContext.getBean(Class.forName(bd.getBeanClassName()));
            orderProcessMap.put(messageProcess.getKey(), messageProcess);
        } catch (Exception e) {
            logger.error(format("Failed to add order process %s to map", bd.getBeanClassName()), e);
        }
    }

    public MessageProcess lookup(ProcessableItem pi) {
        ProcessKey key = pi.getProcessKey();
        logger.debug("Looking process for: {}", pi.getId());
        MessageProcess messageProcess = orderProcessMap.get(key);
        if (messageProcess == null) {
            throw new IllegalStateException(format("No order process could be found for %s with key %s", pi.getId(), key.toString()));
        }

        return messageProcess;
    }
}
