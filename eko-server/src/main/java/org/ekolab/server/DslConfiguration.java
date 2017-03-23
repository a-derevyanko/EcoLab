package org.ekolab.server;

/**
 * Created by 777Al on 23.03.2017.
 */

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@ImportAutoConfiguration({JooqAutoConfiguration.class, JooqAutoConfiguration.DslContextConfiguration.class, DataSourceAutoConfiguration.class})
public class DslConfiguration {
}
