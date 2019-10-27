package org.ecolab.server.dao.impl;

import org.ecolab.server.common.Profiles;
import org.ecolab.server.dao.api.TokenRepositoryDao;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.ecolab.server.db.h2.public_.Tables.USERS;
import static org.ecolab.server.db.h2.public_.tables.PersistentLogins.PERSISTENT_LOGINS;

/**
 * Created by 777Al on 28.03.2017.
 */
@Service
@Profile({Profiles.DB.H2, Profiles.DB.POSTGRES})
public class TokenRepositoryDaoImpl implements TokenRepositoryDao {
    private final DSLContext dsl;

    @Autowired
    public TokenRepositoryDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void insertToken(String userName, String series, String token, Date lastUsed) {
        dsl.insertInto(PERSISTENT_LOGINS, PERSISTENT_LOGINS.USER_ID, PERSISTENT_LOGINS.SERIES, PERSISTENT_LOGINS.TOKEN, PERSISTENT_LOGINS.LAST_USED)
                .select(dsl.select(USERS.ID, DSL.val(series), DSL.val(token), DSL.val(LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.systemDefault()))).
                        from(USERS).where(USERS.LOGIN.eq(userName))).execute();
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        dsl.update(PERSISTENT_LOGINS)
                .set(PERSISTENT_LOGINS.TOKEN, tokenValue)
                .set(PERSISTENT_LOGINS.LAST_USED, DSL.val(LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.systemDefault())))
                .where(PERSISTENT_LOGINS.SERIES.eq(series)).execute();
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        var result = dsl.select(PERSISTENT_LOGINS.fields())
                .select(USERS.LOGIN).from(PERSISTENT_LOGINS).join(USERS).on(USERS.ID.eq(PERSISTENT_LOGINS.USER_ID))
                .where(PERSISTENT_LOGINS.SERIES.eq(seriesId)).fetchOne();
        return result == null ? null : result.map(
                record -> new PersistentRememberMeToken(
                        record.get(USERS.LOGIN), record.get(PERSISTENT_LOGINS.SERIES),
                        record.get(PERSISTENT_LOGINS.TOKEN), Date.from(record.get(PERSISTENT_LOGINS.LAST_USED).atZone(ZoneId.systemDefault()).toInstant())));
    }

    @Override
    public void removeTokensByUser(String userName) {
        dsl.deleteFrom(PERSISTENT_LOGINS)
                .where(PERSISTENT_LOGINS.USER_ID.eq(dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName)))).execute();
    }
}
