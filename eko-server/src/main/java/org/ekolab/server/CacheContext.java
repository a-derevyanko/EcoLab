package org.ekolab.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

/**
 * Created by Андрей on 11.09.2016.
 */
@SpringBootConfiguration
public class CacheContext {
    private final CacheManager cacheManager;

    @Autowired
    public CacheContext(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Bean
    public UserCache userCache() throws Exception {
        return new SpringCacheBasedUserCache(cacheManager.getCache(UserCache.class.getSimpleName()));
    }
}
