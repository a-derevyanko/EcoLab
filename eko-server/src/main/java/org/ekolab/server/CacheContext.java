package org.ekolab.server;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;

/**
 * Created by Андрей on 11.09.2016.
 */
@Configuration
@EnableCaching
public class CacheContext extends JCacheConfigurerSupport {
    public static final String SERVER_CACHE = "ServerCache";
    public static final String USER_CACHE = "UserCache";

    @Bean
    public CacheConfiguration serverCacheConfig() {
        return new CacheConfiguration(SERVER_CACHE, 0);
    }

    @Bean
    public CacheConfiguration userCacheConfig() {
        return new CacheConfiguration(USER_CACHE, 1000);
    }

    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        return net.sf.ehcache.CacheManager.newInstance(
                new net.sf.ehcache.config.Configuration().cache(userCacheConfig())
                        .defaultCache(serverCacheConfig()));
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    public UserCache userCache() {
        EhCacheBasedUserCache userCache = new EhCacheBasedUserCache();
        userCache.setCache(ehCacheManager().getCache(USER_CACHE));
        return userCache;
    }
}
