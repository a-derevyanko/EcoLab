package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.StudentTeam;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.ekolab.server.CacheContext.STUDENT_INFO_CACHE;
import static org.ekolab.server.CacheContext.STUDENT_TEACHERS_CACHE;
import static org.ekolab.server.CacheContext.TEAM_MEMBERS_CACHE;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
public class StudentInfoServiceImpl implements StudentInfoService {
    private final UserInfoService userInfoService;

    private final StudentInfoDao studentInfoDao;

    public StudentInfoServiceImpl(UserInfoService userInfoService, StudentInfoDao studentInfoDao) {
        this.userInfoService = userInfoService;
        this.studentInfoDao = studentInfoDao;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = STUDENT_INFO_CACHE, key = "#userName")
    @Nullable
    public StudentInfo getStudentInfo(String userName) {
        String group = studentInfoDao.getStudentGroup(userName);
        if (group == null) {
            return null;
        } else {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setGroup(group);
            studentInfo.setTeam(studentInfoDao.getStudentTeam(userName));
            return studentInfo;
        }
    }

    @Override
    @Transactional
    @CachePut(cacheNames = STUDENT_INFO_CACHE, key = "#userInfo.login")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, allEntries = true)
    @Deprecated // todo подумать, нужен ли этот метод. кэш для getTeamMembers() плохо будет работаь
    public StudentInfo updateStudentInfo(UserInfo userInfo, String group, Integer number) {
        userInfoService.updateUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), number);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        StudentTeam team = new StudentTeam();
        team.setNumber(number);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = {STUDENT_INFO_CACHE, STUDENT_TEACHERS_CACHE}, key = "#userInfo.login")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, key = "#number")
    @NotNull
    public StudentInfo createStudentInfo(@NotNull UserInfo userInfo, @NotNull String group, @NotNull Integer number, @NotNull String teacherName) {
        if (userInfo.getId() == null) {
            userInfoService.createUserInfo(userInfo);
        }
        studentInfoDao.addTeacherToStudent(userInfo.getLogin(), teacherName);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), number);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        StudentTeam team = new StudentTeam();
        team.setNumber(number);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    public void createStudentGroup(String name) {
        studentInfoDao.createStudentGroup(name);
    }

    @Override
    @Transactional
    public void createStudentTeam(Integer number) {
        studentInfoDao.createStudentTeam(number);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = STUDENT_INFO_CACHE, allEntries = true)
    public void renameStudentGroup(String name, String newName) {
        studentInfoDao.renameStudentGroup(name, newName);
    }

    @Override
    @Cacheable(STUDENT_TEACHERS_CACHE)
    @Transactional(readOnly = true)
    public String getStudentTeacher(String studentLogin) {
        return studentInfoDao.getStudentTeacher(studentLogin);
    }

    @Override
    @Cacheable(TEAM_MEMBERS_CACHE)
    @Transactional(readOnly = true)
    public List<String> getTeamMembers(Integer teamNumber) {
        return studentInfoDao.getTeamMembers(teamNumber);
    }
}
