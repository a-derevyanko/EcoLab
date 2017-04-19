package org.ekolab.server.dao.impl;

import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.dao.api.Lab3Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.testng.annotations.Test;

/**
 * Created by 777Al on 19.04.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class Lab3DaoTest extends AbstractTestWithUser {
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