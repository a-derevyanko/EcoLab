package org.ekolab.client.vaadin.server.service;

import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 30.05.2017.
 */
@Service
@Profile(Profiles.MODE.PROD)
public class EmptyParameterCustomizer implements ParameterCustomizer {
    /**
     * В prod версии никаких префиксов не добавляем.
     * @return пустой префикс.
     */
    @Override
    public String getParameterPrefix() {
        return "";
    }
}
