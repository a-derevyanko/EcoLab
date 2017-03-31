package org.ekolab.server.dao.api;


import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
public interface TokenRepositoryDao {
    void insertToken(String userName, String series, String token, Date lastUsed);

    void updateToken(String series, String tokenValue, Date lastUsed);

    PersistentRememberMeToken getTokenForSeries(String seriesId);

    void removeTokensByUser(String username);
}
