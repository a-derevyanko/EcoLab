package org.ekolab.server.service.api;

import org.apache.commons.lang.RandomStringUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 24.05.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class StudentInfoServiceTest extends AbstractTestWithUser {
    private final String GROUPNAME = "testGroup_" + RandomStringUtils.randomAlphabetic(5);
    private final String TEAMNAME = "testTeam_" + RandomStringUtils.randomAlphabetic(5);

    @Autowired
    private StudentInfoService studentInfoService;

    @BeforeClass
    @Override
    public void generateInitialData() {
        studentInfoService.createStudentTeam(TEAMNAME);
        studentInfoService.createStudentGroup(GROUPNAME);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setTeam(TEAMNAME);
        studentInfo.setGroup(GROUPNAME);
        UserInfo userDetails = new UserInfo();
        userDetails.setLogin(USERNAME);
        userDetails.setGroup(UserGroup.STUDENT);
        studentInfoService.createStudentInfo(userDetails, studentInfo);
    }

    @Test
    public void testGetStudentInfo() throws Exception {
        StudentInfo studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), GROUPNAME);
        Assert.assertEquals(studentInfo.getTeam(), TEAMNAME);
    }

    @Test
    public void testSaveStudentInfo() throws Exception {
        String newGroupName = GROUPNAME + RandomStringUtils.randomAlphabetic(5);
        String newTeamName = TEAMNAME + RandomStringUtils.randomAlphabetic(5);
        studentInfoService.renameStudentGroup(GROUPNAME, newGroupName);
        studentInfoService.renameStudentTeam(TEAMNAME, newTeamName);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setTeam(newTeamName);
        studentInfo.setGroup(newGroupName);
        studentInfoService.updateStudentInfo(userInfoService.getUserInfo(USERNAME), studentInfo);
        studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), newGroupName);
        Assert.assertEquals(studentInfo.getTeam(), newTeamName);
    }

}