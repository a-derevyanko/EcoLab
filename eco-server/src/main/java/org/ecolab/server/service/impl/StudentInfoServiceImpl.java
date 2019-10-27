package org.ecolab.server.service.impl;

import org.ecolab.server.dao.api.content.StudentInfoDao;
import org.ecolab.server.model.StudentGroup;
import org.ecolab.server.model.StudentGroupInfo;
import org.ecolab.server.model.StudentInfo;
import org.ecolab.server.model.StudentTeam;
import org.ecolab.server.model.UserInfo;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static org.ecolab.server.CacheContext.STUDENT_INFO_CACHE;
import static org.ecolab.server.CacheContext.TEAM_MEMBERS_CACHE;

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
        var group = studentInfoDao.getStudentGroup(userName);
        if (group == null) {
            return null;
        } else {
            var studentInfo = new StudentInfo();
            studentInfo.setGroup(group);
            studentInfo.setTeam(studentInfoDao.getStudentTeam(userName));
            return studentInfo;
        }
    }

    @Override
    @Transactional
    @CachePut(cacheNames = STUDENT_INFO_CACHE, key = "#userInfo.login")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, allEntries = true, condition =  "#team != null")
    @Deprecated // todo подумать, нужен ли этот метод. кэш для getTeamMembers() плохо будет работаь
    public StudentInfo updateStudentInfo(UserInfo userInfo, StudentGroup group, StudentTeam team) {
        userInfoService.updateUserInfo(userInfo);
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        studentInfoDao.updateStudentTeam(userInfo.getLogin(), team);
        var studentInfo = new StudentInfo();
        studentInfo.setGroup(group);
        studentInfo.setTeam(team);
        return studentInfo;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = {STUDENT_INFO_CACHE}, key = "#userInfo.login")
    @CacheEvict(cacheNames = TEAM_MEMBERS_CACHE, key = "#team.name", condition =  "#team != null")
    @NotNull
    public StudentInfo createStudentInfo(@NotNull UserInfo userInfo, @NotNull StudentGroup group,
                                         @NotNull StudentTeam team) {
        if (userInfo.getId() == null) {
            userInfoService.createUserInfo(userInfo);
        }
        studentInfoDao.updateStudentGroup(userInfo.getLogin(), group);
        if (team != null) {
            studentInfoDao.updateStudentTeam(userInfo.getLogin(), team);
        }
        var studentInfo = new StudentInfo();
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
    public Set<StudentGroupInfo> getTeacherGroupsInfo(String teacher) {
        return studentInfoDao.getTeacherGroupsInfo(teacher);
    }

    @Override
    public boolean isGroupExists(String group) {
        return studentInfoDao.isGroupExists(group);
    }

    @Override
    public boolean isTeamExists(StudentGroup group, String team) {
        return studentInfoDao.isTeamExists(group, team);
    }

    @Override
    @Transactional
    public void addGroupToTeacher(String teacher, StudentGroup group) {
        studentInfoDao.addGroupToTeacher(teacher, group);
    }

    @Override
    @Transactional
    public void removeGroupFromTeacher(String teacher, String group) {
        studentInfoDao.removeGroupFromTeacher(teacher, group);
    }

    @Override
    public Set<StudentGroup> getStudentGroups() {
        return studentInfoDao.getStudentGroups();
    }

    @Override
    public Set<StudentGroup> getGroupsAvailableForTeacher() {
        return studentInfoDao.getGroupsAvailableForTeacher();
    }

    @Override
    public StudentGroup getStudentGroupByName(String group) {
        return studentInfoDao.getStudentGroupByName(group);
    }

    @Override
    public Set<StudentTeam> getStudentTeams(String group) {
        return studentInfoDao.getStudentTeams(group);
    }

    @Override
    public Set<Integer> getAllowedLabs(String userName) {
        return studentInfoDao.getAllowedLabs(userName);
    }

    @Override
    public Set<Integer> getAllowedDefence(String userName) {
        return studentInfoDao.getAllowedDefence(userName);
    }

    @Override
    @Transactional
    public void changeLabAllowance(String userName, boolean allow, int... labs) {
        studentInfoDao.changeLabAllowance(userName, allow, labs);
    }

    @Override
    @Transactional
    public void changeDefenceAllowance(String userName, boolean allow, int... labs) {
        studentInfoDao.changeDefenceAllowance(userName, allow, labs);
    }
}
