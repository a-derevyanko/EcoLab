package org.ecolab.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jmx.MBeanUtil;
import org.ecolab.server.common.Profiles;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Created by Андрей on 06.09.2016.
 */
@SpringBootConfiguration
@Profile({Profiles.MODE.PROD, Profiles.MODE.PROD})
public class SpringBootAdminContext {
    /**
     * Бин, необходимы для возможности включения/отключения логгирования через админку Spring Boot Admin
     * Возможно, станет ненужным, когда примут ПР {@link <a href="https://github.com/spring-projects/spring-boot/pull/4486">Spring Boot</a> }
     * @param mbs
     * @return
     */
    @Bean
    public InitializingBean jmx(final MBeanServer mbs) {
        return () -> {
            var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            var objectName = new ObjectName(MBeanUtil.getObjectNameFor(loggerContext.getName(), JMXConfigurator.class));
            var jc = new JMXConfigurator(loggerContext, mbs, objectName);
            mbs.registerMBean(jc, objectName);
        };
    }
}

