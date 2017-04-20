package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.TokenRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
@Service
@Transactional
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
    @Autowired
    private TokenRepositoryDao tokenRepositoryDao;

    @Override
    public void createNewToken(@NotNull PersistentRememberMeToken token) {
        tokenRepositoryDao.insertToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        tokenRepositoryDao.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return tokenRepositoryDao.getTokenForSeries(seriesId);
    }

    @Override
    public void removeUserTokens(String username) {
        tokenRepositoryDao.removeTokensByUser(username);
    }
}
