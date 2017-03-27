package org.ekolab.server.service;

import org.ekolab.server.dao.api.TokenRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
@Service
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
    @Autowired
    private TokenRepositoryDao tokenRepositoryDao;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        tokenRepositoryDao.insertToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return null;
    }

    @Override
    public void removeUserTokens(String username) {

    }
}
