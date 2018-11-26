package org.ecolab.server.service.api.content.lab3;

import java.time.LocalDateTime;
import org.apache.commons.lang.math.RandomUtils;
import org.ecolab.server.AbstractTestWithUser;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
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
        createdLab = lab3Service.startNewLab(userInfo.getId());
    }

    @AfterClass
    public void removeLab() {
        lab3Service.removeLabsByUser(userInfo.getId());
    }

    @Test
    public void testGetLabByUser() throws Exception {
        Lab3Data lab3Data = lab3Service.getLastUncompletedLabByUser(userInfo.getId());
        Assert.assertTrue(new ReflectionEquals(lab3Data).matches(createdLab));
    }

    @Test
    public void testUpdateLab() throws Exception {
        Assert.assertNotSame(createdLab.getSaveDate(), lab3Service.updateLab(createdLab));
    }

    @Test
    public void testRemoveOldLabs() throws InterruptedException {
        LocalDateTime startDate = LocalDateTime.now();
        Thread.sleep(1000L);
        int count = RandomUtils.nextInt(15);
        for (int i = 0; i < count; i++) {
            lab3Service.startNewLab(userInfo.getId());
        }
        Assert.assertEquals(lab3Service.removeOldLabs(startDate), count);
    }
}