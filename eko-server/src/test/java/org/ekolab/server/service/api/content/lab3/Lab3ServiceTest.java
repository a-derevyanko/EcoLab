package org.ekolab.server.service.api.content.lab3;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by Андрей on 01.05.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class Lab3ServiceTest extends AbstractTestWithUser {
    private Lab3Data createdLab;

    @Autowired
    private Lab3Service lab3Service;

    @BeforeClass
    public void generateLab() {
        createdLab = lab3Service.startNewLab(USERNAME);
    }

    @AfterClass
    public void removeLab() {
        lab3Service.removeLabsByUser(USERNAME);
    }

    @Test
    public void testGetLabByUser() throws Exception {
        Lab3Data lab3Data = lab3Service.getLastUncompletedLabByUser(USERNAME);
        Assert.assertTrue(new ReflectionEquals(lab3Data).matches(createdLab));
    }

    @Test
    public void testUpdateLab() throws Exception {
        Assert.assertNotSame(createdLab.getSaveDate(), lab3Service.updateLab(createdLab));
    }

    @Test
    public void testGetAllLabsByUser() {
        Assert.assertEquals(lab3Service.getAllLabsByUser(USERNAME).size(), 1);
    }

    @Test(timeOut = 1000L)
    public void testRemoveOldLabs() {
        int count = RandomUtils.nextInt(15);
        for (int i = 0; i < count; i++) {
            lab3Service.startNewLab(USERNAME);
        }
        Assert.assertEquals(lab3Service.removeOldLabs(createdLab.getSaveDate()), count);
    }
}