package org.ecolab.server;

import org.apache.commons.lang.RandomStringUtils;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.UserInfoService;
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
@SpringBootTest(classes = {ServerApplication.class, TestServerConfiguration.class})
@ActiveProfiles({Profiles.MODE.TEST, Profiles.DB.H2})
@Transactional
@Rollback
public abstract class AbstractTestWithUser extends AbstractTestNGSpringContextTests {
    protected UserInfo userInfo;

    @Autowired
    protected UserInfoService userInfoService;

    @BeforeClass
    public void generateInitialData() {
        String username = "testUser_" + RandomStringUtils.randomAlphabetic(5);
        UserInfo userDetails = new UserInfo();
        userDetails.setLogin(username);
        userDetails.setFirstName(username);
        userDetails.setLastName(username);
        userDetails.setMiddleName(username);
        userDetails.setGroup(UserGroup.ADMIN);
        userInfo = userInfoService.createUserInfo(userDetails);
    }

    @AfterClass
    public void removeUser() {
        userInfoService.deleteUser(userInfo.getLogin());
    }
}
