package org.ecolab.server.service.impl;

import org.ecolab.server.service.api.ConfigurationCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ConfigurationCommonServiceImpl implements ConfigurationCommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCommonServiceImpl.class);

    private final ApplicationContext appContext;

    @Autowired
    public ConfigurationCommonServiceImpl(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    @Async
    public void printApplicationBeanNames() {
        var builder = new StringBuilder("Beans provided by Spring Boot for Server:\n");

        var beanNames = appContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (var beanName : beanNames) {
            builder.append(beanName).append('\n');
        }
        builder.append("-------------------------------------------------------\n");
        builder.append("|                  Server metrics                     |\n");
        builder.append("|                  Beans count: ").append(appContext.getBeanDefinitionCount()).append("                   |\n");
        builder.append("-------------------------------------------------------\n");
        LOGGER.info(builder.toString());
    }
}
