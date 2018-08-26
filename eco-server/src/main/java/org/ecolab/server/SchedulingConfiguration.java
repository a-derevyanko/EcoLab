package org.ecolab.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Андрей on 01.05.2017.
 */
@SpringBootConfiguration
@EnableAsync
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer, AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfiguration.class);

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ScheduledExecutorService delegateExecutor = Executors.newScheduledThreadPool(100);
        return new DelegatingSecurityContextScheduledExecutorService(delegateExecutor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(getAsyncExecutor());
    }
}
