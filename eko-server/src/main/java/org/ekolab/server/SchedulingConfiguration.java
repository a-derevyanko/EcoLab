package org.ekolab.server;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.scheduling.DelegatingSecurityContextSchedulingTaskExecutor;

/**
 * Created by Андрей on 01.05.2017.
 */
@SpringBootConfiguration
@EnableScheduling
public class SchedulingConfiguration {
    @Bean
    public SchedulingTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(100);
        taskScheduler.initialize();
        return new DelegatingSecurityContextSchedulingTaskExecutor(taskScheduler);
    }
}
