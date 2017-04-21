package org.ekolab.server;

import org.apache.commons.lang.RandomStringUtils;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Collections;

/**
 * Created by 777Al on 19.04.2017.
 */
@SpringBootTest(classes = {ServerApplication.class})
@ActiveProfiles({Profiles.MODE.TEST, Profiles.DB.H2})
@Transactional
@Rollback
public abstract class AbstractTestWithUser extends AbstractTestNGSpringContextTests {
    protected static final String USERNAME = "testUser_" + RandomStringUtils.randomAlphabetic(5);

    @Autowired
    private UserDetailsManager userDetailsManager;

    @BeforeClass
    public void generateUser() {
        userDetailsManager.createUser(new User(USERNAME, "password", Collections.emptyList()));
    }

    @AfterClass
    public void removeUser() {
        userDetailsManager.deleteUser(USERNAME);
    }
}
