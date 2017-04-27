package org.ekolab.server.dao.impl.content;

import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.model.LabData;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;

import static org.ekolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabDaoImpl<T extends LabData> implements LabDao<T> {
    @Autowired
    protected DSLContext dsl;

    protected Select<Record1<Long>> getFindUserIdSelect(String userName) {
        return dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName));
    }
}
