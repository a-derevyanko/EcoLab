package org.ecolab.server.service.api.content.lab1;

import org.apache.commons.lang.math.RandomUtils;
import org.ecolab.server.AbstractTestWithUser;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
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
public abstract class Lab1ServiceTest<V extends Lab1Variant> extends AbstractTestWithUser {
    private Lab1Data<V> createdLab;

    private final Lab1Service<V> lab1Service;

    public Lab1ServiceTest(Lab1Service<V> lab1Service) {
        this.lab1Service = lab1Service;
    }

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