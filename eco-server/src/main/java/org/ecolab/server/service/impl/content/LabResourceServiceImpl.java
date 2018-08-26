package org.ecolab.server.service.impl.content;

import org.ecolab.server.service.api.content.LabResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class LabResourceServiceImpl implements LabResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LabResourceServiceImpl.class);

    @Override
    @Cacheable("COAT_OF_ARMS_URL_CACHE")
    public URL getCoatOfArms(String city) {
        return LabServiceImpl.class.getResource("coatofarms/" + city + ".png");
    }
}
