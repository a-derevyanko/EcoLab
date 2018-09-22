package org.ecolab.server.service.impl;

import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

import java.util.concurrent.Executors;

class SystemTaskExecutor extends DelegatingSecurityContextExecutor {
    static final SystemTaskExecutor INSTANCE = new SystemTaskExecutor();

    private SystemTaskExecutor() {
        super(Executors.newFixedThreadPool(100), SystemAuthentication.SECURITY_CONTEXT_INSTANCE);

    }
}
