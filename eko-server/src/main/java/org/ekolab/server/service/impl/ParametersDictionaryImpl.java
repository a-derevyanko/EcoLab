package org.ekolab.server.service.impl;

import org.ekolab.server.service.api.ParametersDictionary;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 30.03.2017.
 */
@Service
public class ParametersDictionaryImpl implements ParametersDictionary {
    @Override
    @NotNull
    @Cacheable("parameters")
    public String getParameterDescription(@NotNull String parameterName) {
        return null;
    }
}
