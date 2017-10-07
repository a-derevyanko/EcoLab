package org.ekolab.server.service.impl;

import org.ekolab.server.service.api.ConfigurationCommonService;
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

    @Autowired
    private ApplicationContext appContext;

    @Override
    @Async
    public void printApplicationBeanNames() {
        StringBuilder builder = new StringBuilder("Beans provided by Spring Boot for Server:\n");

        String[] beanNames = appContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            builder.append(beanName).append('\n');
        }
        builder.append("-------------------------------------------------------\n");
        builder.append("|                  Server metrics                     |\n");
        builder.append("|                  Beans count: ").append(appContext.getBeanDefinitionCount()).append("                   |\n");
        builder.append("-------------------------------------------------------\n");
        LOGGER.info(builder.toString());
    }
}
