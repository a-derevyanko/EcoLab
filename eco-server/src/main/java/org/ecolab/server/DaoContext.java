package org.ecolab.server;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

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
}

