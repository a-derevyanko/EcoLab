package org.ekolab.server.dao.impl;

import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.dao.api.Lab3Dao;
import org.ekolab.server.model.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

/**
 * Created by 777Al on 19.04.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class Lab3DaoTest extends AbstractTestWithUser {
    private Lab3Data createdLab;

    @Autowired
    private Lab3Dao lab3Dao;

    @BeforeClass
    public void generateLab() {
        Lab3Data data = new Lab3Data();
        data.setUserLogin(USERNAME);
        data.setStartDate(LocalDateTime.now());
        data.setSaveDate(LocalDateTime.now());
        createdLab = lab3Dao.saveLab(data);
    }

    @AfterClass
    public void removeLab() {
        lab3Dao.removeLabsByUser(USERNAME);
    }

    @Test(enabled = false)
    public void testGetLabByUser() throws Exception {
        Lab3Data lab3Data = lab3Dao.getLastLabByUser(USERNAME);
        Assert.assertEquals(lab3Data, createdLab);
    }

    @Test(enabled = false)
    public void testUpdateLab() throws Exception {
        createdLab.setSaveDate(LocalDateTime.now());
        Lab3Data updatedLab = lab3Dao.updateLab(createdLab);
        Assert.assertEquals(updatedLab, createdLab);
    }

    @Test
    public void testGetAllLabsByUser() {

    }

    @Test
    public void testRemoveOldLabs() {
        lab3Dao.removeOldLabs(LocalDateTime.now().toLocalDate().atStartOfDay());
    }
}