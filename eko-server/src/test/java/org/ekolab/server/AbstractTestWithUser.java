package org.ekolab.server;

import org.apache.commons.lang.RandomStringUtils;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Created by 777Al on 19.04.2017.
 */
@SpringBootTest(classes = {ServerApplication.class})
@ActiveProfiles({Profiles.MODE.TEST, Profiles.DB.H2})
@Transactional
@Rollback
public abstract class AbstractTestWithUser extends AbstractTestNGSpringContextTests {
    protected final String USERNAME = "testUser_" + RandomStringUtils.randomAlphabetic(5);

    @Autowired
    protected UserInfoService userInfoService;

    @BeforeClass
    public void generateInitialData() {
        UserInfo userDetails = new UserInfo();
        userDetails.setLogin(USERNAME);
        userDetails.setFirstName(USERNAME);
        userDetails.setLastName(USERNAME);
        userDetails.setMiddleName(USERNAME);
        userDetails.setGroup(UserGroup.ADMIN);
        userInfoService.createUserInfo(userDetails);
    }

    @AfterClass
    public void removeUser() {
        userInfoService.deleteUser(USERNAME);
    }
}
