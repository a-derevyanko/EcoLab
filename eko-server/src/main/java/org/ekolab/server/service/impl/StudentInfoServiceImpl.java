package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentGroup;
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
import java.util.Set;

import static org.ekolab.server.CacheContext.STUDENT_INFO_CACHE;
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
        StudentGroup group = studentInfoDao.getStudentGroup(userName);
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
    public StudentInfo updateStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team) {
        userInfoService.updateUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), team);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = {STUDENT_INFO_CACHE}, key = "#userInfo.login")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, key = "#team")
    @NotNull
    public StudentInfo createStudentInfo(@NotNull UserInfo userInfo, @NotNull StudentGroup group,
                                         @NotNull StudentTeam team) {
        if (userInfo.getId() == null) {
            userInfoService.createUserInfo(userInfo);
        }
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), team);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    public StudentGroup createStudentGroup(String name) {
        return studentInfoDao.createStudentGroup(name);
    }

    @Override
    @Transactional
    public StudentTeam createStudentTeam(String name, String group) {
        return studentInfoDao.createStudentTeam(name, group);
    }

    @Override
    @Transactional
    public void removeStudentGroup(String name) {
        studentInfoDao.removeStudentGroup(name);
    }

    @Override
    @Transactional
    public void removeStudentTeam(String name, String group) {
        studentInfoDao.removeStudentTeam(name, group);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = STUDENT_INFO_CACHE, allEntries = true)
    public void updateStudentGroup(StudentGroup group) {
        studentInfoDao.updateStudentGroup(group);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getGroupTeacher(String group) {
        return userInfoService.getUserInfo(studentInfoDao.getGroupTeacher(group));
    }

    @Override
    @Cacheable(TEAM_MEMBERS_CACHE)
    @Transactional(readOnly = true)
    public Set<String> getTeamMembers(String name, String group) {
        return studentInfoDao.getTeamMembers(name, group);
    }

    @Override
    public Set<String> getGroupMembers(String group) {
        return studentInfoDao.getGroupMembers(group);
    }

    @Override
    public Set<StudentGroup> getTeacherGroups(String teacher) {
        return studentInfoDao.getTeacherGroups(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<StudentGroup> getStudentGroups() {
        return studentInfoDao.getStudentGroups();
    }

    @Override
    public Set<StudentTeam> getStudentTeams(String group) {
        return studentInfoDao.getStudentTeams(group);
    }
}
