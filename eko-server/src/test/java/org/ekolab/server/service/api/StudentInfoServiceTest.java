package org.ekolab.server.service.api;

import org.apache.commons.lang.RandomStringUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.StudentGroup;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.StudentTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 24.05.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class StudentInfoServiceTest extends AbstractTestWithUser {
    private final String GROUPNAME = "testGroup_" + RandomStringUtils.randomAlphabetic(5);
    private final String TEAMNUMBER = "testTeam_" + RandomStringUtils.randomAlphabetic(5);
    private StudentGroup testGroup;
    private StudentTeam testTeam;

    @Autowired
    private StudentInfoService studentInfoService;

    @BeforeClass
    @Override
    public void generateInitialData() {
        super.generateInitialData();
        testGroup = studentInfoService.createStudentGroup(GROUPNAME);
        testTeam = studentInfoService.createStudentTeam(TEAMNUMBER, GROUPNAME);
        studentInfoService.createStudentInfo(userInfoService.getUserInfo(USERNAME), testGroup, testTeam, "");
    }

    @AfterClass
    @Override
    public void removeUser() {
        super.removeUser();
        studentInfoService.removeStudentTeam(TEAMNUMBER, GROUPNAME);
        studentInfoService.removeStudentGroup(GROUPNAME);
    }

    @Test
    public void testGetStudentInfo() {
        StudentInfo studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), testGroup);
        Assert.assertEquals(studentInfo.getTeam(), testTeam);
    }

    @Test
    public void testSaveStudentInfo() {
        String newGroupName = GROUPNAME + RandomStringUtils.randomAlphabetic(5);
        testGroup.setName(newGroupName);
        studentInfoService.updateStudentGroup(testGroup);
        studentInfoService.updateStudentInfo(userInfoService.getUserInfo(USERNAME), testGroup, testTeam);
        StudentInfo studentInfo = studentInfoService.getStudentInfo(USERNAME);
        Assert.assertEquals(studentInfo.getGroup(), testGroup);
        Assert.assertEquals(studentInfo.getTeam().getName(), TEAMNUMBER);
    }

}