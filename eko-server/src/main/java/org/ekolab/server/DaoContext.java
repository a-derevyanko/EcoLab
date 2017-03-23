package org.ekolab.server;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Андрей on 06.09.2016.
 */
@SpringBootConfiguration
public class DaoContext {
    private final DSLContext dsl;

    @Autowired
    public DaoContext(DSLContext dsl) {
        this.dsl = dsl;
    }

    /*@Bean
    public StaffDao staffDao() {
        return new StaffDaoImpl(em);
    }*/
}

