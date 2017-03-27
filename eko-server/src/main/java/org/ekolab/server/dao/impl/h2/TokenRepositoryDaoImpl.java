package org.ekolab.server.dao.impl.h2;

import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.TokenRepositoryDao;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.ekolab.server.db.h2.public_.Tables.USERS;
import static org.ekolab.server.db.h2.public_.tables.PersistentLogins.PERSISTENT_LOGINS;

/**
 * Created by 777Al on 28.03.2017.
 */
@Service
@Profile(Profiles.DB.H2)
public class TokenRepositoryDaoImpl implements TokenRepositoryDao {
    @Autowired
    private DSLContext dsl;

    public void insertToken(String userName, String series, String token, Date lastUsed) {
        dsl.insertInto(PERSISTENT_LOGINS, PERSISTENT_LOGINS.USER_ID, PERSISTENT_LOGINS.SERIES, PERSISTENT_LOGINS.TOKEN, PERSISTENT_LOGINS.LAST_USED)
                .select(dsl.select(USERS.ID, DSL.val(series), DSL.val(token), DSL.timestamp(lastUsed)).from(USERS).where(USERS.LOGIN.eq(userName))).execute();
    }
}
