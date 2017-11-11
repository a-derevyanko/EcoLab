package org.ekolab.server.service.api.content.lab1;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.AbstractTestWithUser;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.random.Lab1RandomService;
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
public class Lab1RandomServiceTest extends Lab1ServiceTest<Lab1Variant> {
    public Lab1RandomServiceTest(Lab1RandomService lab1Service) {
        super(lab1Service);
    }
}