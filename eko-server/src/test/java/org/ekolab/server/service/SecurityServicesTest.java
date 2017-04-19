package org.ekolab.server.service;

import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.ServerApplication;
import org.ekolab.server.common.Profiles;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
@SpringBootTest(classes = {ServerApplication.class})
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
@ActiveProfiles({Profiles.MODE.TEST, Profiles.DB.H2})
@Transactional
@Rollback
public class SecurityServicesTest extends AbstractTestWithUser {
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Test
    public void testPersistentTokenRepository() throws Exception {
        PersistentRememberMeToken token = new PersistentRememberMeToken(USERNAME, "series", "tokenValue", new Date());
        persistentTokenRepository.createNewToken(token);

        Assert.assertTrue(new ReflectionEquals(persistentTokenRepository.getTokenForSeries("series")).matches(token));
        persistentTokenRepository.updateToken( "series", "tokenValueModified", new Date());
        Assert.assertEquals("tokenValueModified", persistentTokenRepository.getTokenForSeries("series").getTokenValue());
        persistentTokenRepository.removeUserTokens( "user");
        Assert.assertNull(persistentTokenRepository.getTokenForSeries("series"));
    }
}