package org.ekolab.server.service;

import org.ekolab.server.ServerApplication;
import org.ekolab.server.common.Profiles;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;

/**
 * Created by 777Al on 28.03.2017.
 */
@SpringBootTest(classes = {ServerApplication.class})
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
@ActiveProfiles(Profiles.MODE.TEST)
@Transactional
@Rollback
public class SecurityServicesTest extends AbstractTestNGSpringContextTests {
    private static final String USERNAME = "user";

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private UserDetailsManager userDetailsManager;

    @BeforeClass
    public void generateUser() {
        userDetailsManager.createUser(new User(USERNAME, "password", Collections.emptyList()));
    }

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

    @AfterClass
    public void removeUser() {
        userDetailsManager.deleteUser(USERNAME);
    }
}