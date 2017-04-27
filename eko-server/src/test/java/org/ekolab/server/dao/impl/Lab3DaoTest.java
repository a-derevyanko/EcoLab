package org.ekolab.server.dao.impl;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.dao.api.content.lab3.Lab3Dao;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
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
        createdLab = new Lab3Data();
        createdLab.setUserLogin(USERNAME);
        createdLab.setStartDate(LocalDateTime.now());
        createdLab.setSaveDate(LocalDateTime.now());
        lab3Dao.saveLab(createdLab);
    }

    @AfterClass
    public void removeLab() {
        lab3Dao.removeLabsByUser(USERNAME);
    }

    @Test
    public void testGetLabByUser() throws Exception {
        Lab3Data lab3Data = lab3Dao.getLastLabByUser(USERNAME);
        Assert.assertTrue(new ReflectionEquals(lab3Data).matches(createdLab));
    }

    @Test
    public void testUpdateLab() throws Exception {
        createdLab.setSaveDate(LocalDateTime.now());
        Assert.assertEquals(lab3Dao.updateLab(createdLab), 1);
    }

    @Test
    public void testGetAllLabsByUser() {
        Assert.assertEquals(lab3Dao.getAllLabsByUser(USERNAME).size(), 1);
    }

    @Test
    public void testRemoveOldLabs() {
        LocalDateTime dateTime = LocalDateTime.now();
        int count = RandomUtils.nextInt(15);
        for (int i = 0; i < count; i++) {
            Lab3Data lab3Data = new Lab3Data();
            lab3Data.setUserLogin(USERNAME);
            lab3Data.setStartDate(dateTime);
            lab3Data.setSaveDate(dateTime);
            lab3Dao.saveLab(lab3Data);
        }
        Assert.assertEquals(count, lab3Dao.removeOldLabs(dateTime));
    }
}