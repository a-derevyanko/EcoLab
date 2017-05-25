package org.ekolab.server.service.impl;

import org.ekolab.server.dao.api.content.StudentInfoDao;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public StudentInfo getStudentInfo(String userName) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setGroup(studentInfoDao.getStudentGroup(userName));
        studentInfo.setTeam(studentInfoDao.getStudentTeam(userName));
        return studentInfo;
    }

    @Override
    @Transactional
    public void updateStudentInfo(UserInfo userInfo, StudentInfo studentInfo) {
        userInfoService.updateUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), studentInfo.getGroup());
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), studentInfo.getTeam());
    }

    @Override
    @Transactional
    public void createStudentInfo(UserInfo userInfo, StudentInfo studentInfo) {
        userInfoService.createUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), studentInfo.getGroup());
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), studentInfo.getTeam());
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
    public void renameStudentGroup(String name, String newName) {
        studentInfoDao.renameStudentGroup(name, newName);
    }

    @Override
    @Transactional
    public void renameStudentTeam(String name, String newName) {
        studentInfoDao.renameStudentTeam(name, newName);
    }
}
