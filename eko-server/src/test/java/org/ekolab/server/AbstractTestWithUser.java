package org.ekolab.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Collections;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class AbstractTestWithUser extends AbstractTestNGSpringContextTests {
    protected static final String USERNAME = "user";

    @Autowired
    private UserDetailsManager userDetailsManager;

    protected UserDetails userDetails;

    @BeforeClass
    public void generateUser() {
        userDetailsManager.createUser(new User(USERNAME, "password", Collections.emptyList()));
        userDetails = userDetailsManager.loadUserByUsername(USERNAME);
    }

    @AfterClass
    public void removeUser() {
        userDetailsManager.deleteUser(USERNAME);
    }
}
