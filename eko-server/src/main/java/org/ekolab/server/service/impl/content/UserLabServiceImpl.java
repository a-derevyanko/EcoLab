package org.ekolab.server.service.impl.content;

import org.ekolab.server.dao.api.content.UserLabDao;
import org.ekolab.server.service.api.content.UserLabService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserLabServiceImpl implements UserLabService {
    private final UserLabDao dao;

    public UserLabServiceImpl(UserLabDao dao) {
        this.dao = dao;
    }

    @Override
    @Cacheable(value = "COMPLETED_TEST", key = "#userName")
    public Collection<Integer> getCompletedTests(String userName) {
        return dao.getCompletedTests(userName);
    }

    @Override
    public Collection<Integer> getCompletedLabs(String userName) {
        return dao.getCompletedLabs(userName);
    }

    @Override
    @CachePut(value = "COMPLETED_TEST", key = "#userName")
    public Collection<Integer> setTestCompleted(String userName, int labNumber) {
        dao.setTestCompleted(userName, labNumber);
        return dao.getCompletedTests(userName);
    }
}
