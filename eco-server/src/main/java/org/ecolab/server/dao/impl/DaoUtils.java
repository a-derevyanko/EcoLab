package org.ecolab.server.dao.impl;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Select;

import static org.ecolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class DaoUtils {
    /**
     *
     * @deprecated идентификатор пользователя есть в контексте
     */
    @Deprecated
    public static Select<Record1<Long>> getFindUserIdSelect(DSLContext dsl, String userName) {
        return dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName));
    }}
