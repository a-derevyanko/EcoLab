package org.ekolab.server;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;

/**
 * Created by Андрей on 06.09.2016.
 */
@SpringBootConfiguration
public class DaoContext {
    private final DSLContext dsl;
    private final FlywayMigrationInitializer flywayMigrationInitializer;

    @Autowired
    public DaoContext(DSLContext dsl, FlywayMigrationInitializer flywayMigrationInitializer) {
        this.dsl = dsl;
        this.flywayMigrationInitializer = flywayMigrationInitializer;
    }

    /*@Bean
    public StaffDao staffDao() {
        return new StaffDaoImpl(em);
    }*/
}

