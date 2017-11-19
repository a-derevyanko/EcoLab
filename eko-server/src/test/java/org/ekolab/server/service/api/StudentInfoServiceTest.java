package org.ekolab.server.service.api;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.StudentInfo;
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
    private final int TEAMNUMBER = RandomUtils.nextInt();

    @Autowired
    private StudentInfoService studentInfoService;

    @BeforeClass
    @Override
    public void generateInitialData() {
        super.generateInitialData();
        studentInfoService.createStudentTeam(TEAMNUMBER);
        studentInfoService.createStudentGroup(GROUPNAME);
        studentInfoService.createStudentInfo(userInfoService.getUserInfo(USERNAME), GROUPNAME, TEAMNUMBER, "");
    }

    @Test
    public void testGetStudentInfo() throws Exception {
        StudentInfo studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), GROUPNAME);
        Assert.assertEquals(studentInfo.getTeam().getNumber(), TEAMNUMBER);
    }

    @Test
    public void testSaveStudentInfo() throws Exception {
        String newGroupName = GROUPNAME + RandomStringUtils.randomAlphabetic(5);
        int newTeamNumber = TEAMNUMBER + 1;
        studentInfoService.createStudentTeam(newTeamNumber);
        studentInfoService.renameStudentGroup(GROUPNAME, newGroupName);
        studentInfoService.updateStudentInfo(userInfoService.getUserInfo(USERNAME), newGroupName, newTeamNumber);
        StudentInfo studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), newGroupName);
        Assert.assertEquals(studentInfo.getTeam().getNumber(), newTeamNumber);
    }

}