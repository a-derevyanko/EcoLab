package org.ecolab.server.service.api.content.lab1;

import org.ecolab.server.model.content.lab1.random.Lab1RandomVariant;
import org.ecolab.server.service.api.content.lab1.random.Lab1RandomService;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created by Андрей on 01.05.2017.
 */
@EnableAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
public class Lab1RandomServiceTest extends Lab1ServiceTest<Lab1RandomVariant> {
    public Lab1RandomServiceTest(Lab1RandomService lab1Service) {
        super(lab1Service);
    }
}