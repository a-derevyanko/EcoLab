package org.ecolab.server;

/**
 * Created by 777Al on 23.03.2017.
 */

import org.jooq.conf.Settings;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class DslConfiguration {
    @Bean
    public Settings getSettings() {
        var settings = new Settings();
        settings.setExecuteWithOptimisticLocking(true);
        return settings;
    }
}
