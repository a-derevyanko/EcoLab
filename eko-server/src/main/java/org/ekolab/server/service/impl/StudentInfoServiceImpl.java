package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.ekolab.server.CacheContext.STUDENT_INFO_CACHE;

/**
 * Created by 777Al on 24.05.2017.
 */
@Service
public class StudentInfoServiceImpl implements StudentInfoService {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Override
    public boolean isStudent(UserInfo userInfo) {
        return userInfo.getGroup() == UserGroup.STUDENT;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = STUDENT_INFO_CACHE, key = "#userName")
    public StudentInfo getStudentInfo(String userName) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(studentInfoDao.getStudentGroup(userName));
        studentInfo.setTeam(studentInfoDao.getStudentTeam(userName));
        return studentInfo;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = STUDENT_INFO_CACHE, key = "#userInfo.login")
    public StudentInfo updateStudentInfo(UserInfo userInfo, String group, String team) {
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
    @CachePut(cacheNames = STUDENT_INFO_CACHE, key = "#userInfo.login")
    public StudentInfo createStudentInfo(UserInfo userInfo, String group, String team) {
        userInfoService.createUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), team);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
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
    public void createStudentTeam(String name) {
        studentInfoDao.createStudentTeam(name);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = STUDENT_INFO_CACHE, allEntries = true)
    public void renameStudentGroup(String name, String newName) {
        studentInfoDao.renameStudentGroup(name, newName);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = STUDENT_INFO_CACHE, allEntries = true)
    public void renameStudentTeam(String name, String newName) {
        studentInfoDao.renameStudentTeam(name, newName);
    }
}
