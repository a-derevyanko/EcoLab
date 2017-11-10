package org.ekolab.server.service.api.content.lab1;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.model.content.lab1.random.Lab1RandomData;
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
 * Created by Андрей on 01.05.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class Lab1ServiceTest extends AbstractTestWithUser {
    private Lab1RandomData createdLab;

    @Autowired
    private Lab1Service<Lab1RandomData, Lab1Variant> lab1Service;

    @BeforeClass
    public void generateLab() {
        createdLab = lab1Service.startNewLab(USERNAME);
    }

    @AfterClass
    public void removeLab() {
        lab1Service.removeLabsByUser(USERNAME);
    }

    @Test
    public void testGetLabByUser() throws Exception {
        Lab1Data lab1Data = lab1Service.getLastUncompletedLabByUser(USERNAME);
        Assert.assertTrue(new ReflectionEquals(lab1Data).matches(createdLab));
    }

    @Test
    public void testUpdateLab() throws Exception {
        Assert.assertNotSame(createdLab.getSaveDate(), lab1Service.updateLab(createdLab));
    }

    @Test
    public void testGetAllLabsByUser() {
        Assert.assertEquals(lab1Service.getAllLabsByUser(USERNAME).size(), 1);
    }

    @Test
    public void testRemoveOldLabs() throws InterruptedException {
        LocalDateTime startDate = LocalDateTime.now();
        Thread.sleep(1000L);
        int count = RandomUtils.nextInt(15);
        for (int i = 0; i < count; i++) {
            lab1Service.startNewLab(USERNAME);
        }
        Assert.assertEquals(lab1Service.removeOldLabs(startDate), count);
    }
}