package org.ekolab.server.service.impl.content.lab1;

import org.ekolab.server.service.api.content.lab1.Lab1ResourceService;
import org.ekolab.server.service.impl.content.LabResourceServiceImpl;
import org.ekolab.server.service.impl.content.lab1.random.Lab1RandomServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class Lab1ResourceServiceImpl extends LabResourceServiceImpl implements Lab1ResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Lab1ResourceServiceImpl.class);

    @Override
    @Cacheable("BOILER_URL_CACHE")
    public URL getBoiler() {
        return Lab1RandomServiceImpl.class.getResource("boiler.svg");
    }
}
