package org.ekolab.server.dao.impl;

import org.ekolab.server.ServerApplication;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.dao.api.Lab3Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 19.04.2017.
 */
@SpringBootTest(classes = {ServerApplication.class})
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
@ActiveProfiles({Profiles.MODE.TEST, Profiles.DB.H2})
@Transactional
@Rollback
public class Lab3DaoTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private Lab3Dao lab3Dao;

    @Test
    public void testGetLabByUser() throws Exception {

    }

    @Test
    public void testSaveLab() throws Exception {
    }

    @Test
    public void testUpdateLab() throws Exception {
    }

}