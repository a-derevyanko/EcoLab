package org.ekolab.server.service.impl.content;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.UnhandledException;
import org.ekolab.server.dao.api.content.UserLabDao;
import org.ekolab.server.model.LabMode;
import org.ekolab.server.model.UserLabStatistics;
import org.ekolab.server.model.UserProfile;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserLabServiceImpl implements UserLabService {
    private final UserInfoService userInfoService;
    private final StudentInfoService studentInfoService;

    private final UserLabDao dao;

    private final List<LabService<?, ?>> labServices;

    public UserLabServiceImpl(UserInfoService userInfoService, StudentInfoService studentInfoService, UserLabDao dao, List<LabService<?, ?>> labServices) {
        this.userInfoService = userInfoService;
        this.studentInfoService = studentInfoService;
        this.dao = dao;
        this.labServices = labServices;
    }

    @Override
    @Cacheable(value = "COMPLETED_TEST", key = "#userName")
    public Collection<Integer> getCompletedTests(String userName) {
        return dao.getCompletedTests(userName);
    }

    @Override
    public Map<Integer, LabMode> getCompletedLabs(String userName) {
        return dao.getCompletedLabs(userName);
    }

    @Override
    @CachePut(value = "COMPLETED_TEST", key = "#userName")
    @Transactional
    public Collection<Integer> setTestCompleted(String userName, int labNumber, int mark, int pointCount) {
        dao.setTestCompleted(userName, labNumber, mark, pointCount);
        return dao.getCompletedTests(userName);
    }

    @Override
    @Transactional
    public int removeAllOldLabs(LocalDateTime lastSaveDate) {
        AtomicInteger removedLabs = new AtomicInteger();
        labServices.forEach(labService -> removedLabs.addAndGet(labService.removeOldLabs(lastSaveDate)));
        return removedLabs.get();
    }

    @Override
    public UserProfile getUserProfile(@NotNull String userName) {
        UserProfile profile = new UserProfile();
        profile.setStatistics(dao.getUserLabStatistics(userName));
        profile.setUserInfo(userInfoService.getUserInfo(userName));
        profile.setAllowedLabs(studentInfoService.getAllowedLabs(userName));
        profile.setStudentInfo(studentInfoService.getStudentInfo(userName));
        profile.setAverageMark(profile.getStatistics().stream().mapToDouble(UserLabStatistics::getMark).average().orElse(0.0));
        profile.setAveragePointCount(profile.getStatistics().stream().mapToDouble(UserLabStatistics::getPointCount).average().orElse(0.0));

        try {
            if (profile.getAverageMark() < 3) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/1.svg")));
            } else if (profile.getAverageMark() < 4) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/2.svg")));
            } else if (profile.getAverageMark() < 5) {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/3.svg")));
            } else {
                profile.setPicture(IOUtils.toByteArray(UserLabServiceImpl.class.getResource("profile/4.svg")));
            }
        } catch (IOException ex) {
            throw new UnhandledException(ex);
        }
        return profile;
    }

    @Override
    public Set<UserProfile> getUserProfiles(String group) {
        return studentInfoService.getGroupMembers(group).stream().map(this::getUserProfile).collect(Collectors.toSet());
    }
}
