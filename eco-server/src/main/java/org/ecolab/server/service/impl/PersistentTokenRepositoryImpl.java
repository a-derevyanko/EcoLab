package org.ecolab.server.service.impl;

import org.ecolab.server.dao.api.TokenRepositoryDao;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
@Service
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
    private final TokenRepositoryDao tokenRepositoryDao;

    public PersistentTokenRepositoryImpl(TokenRepositoryDao tokenRepositoryDao) {
        this.tokenRepositoryDao = tokenRepositoryDao;
    }

    @Override
    @Transactional
    public void createNewToken(@NotNull PersistentRememberMeToken token) {
        tokenRepositoryDao.insertToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        tokenRepositoryDao.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return tokenRepositoryDao.getTokenForSeries(seriesId);
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        tokenRepositoryDao.removeTokensByUser(username);
    }
}
