package org.ihiw.management.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.ihiw.management.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.ihiw.management.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.ihiw.management.domain.User.class.getName());
            createCache(cm, org.ihiw.management.domain.Authority.class.getName());
            createCache(cm, org.ihiw.management.domain.User.class.getName() + ".authorities");
            createCache(cm, org.ihiw.management.domain.Project.class.getName());
            createCache(cm, org.ihiw.management.domain.Project.class.getName() + ".labs");
            createCache(cm, org.ihiw.management.domain.Project.class.getName() + ".leaders");
            createCache(cm, org.ihiw.management.domain.Project.class.getName() + ".uploads");
            createCache(cm, org.ihiw.management.domain.Upload.class.getName());
            createCache(cm, org.ihiw.management.domain.Upload.class.getName() + ".validations");
            createCache(cm, org.ihiw.management.domain.Validation.class.getName());
            createCache(cm, org.ihiw.management.domain.IhiwLab.class.getName());
            createCache(cm, org.ihiw.management.domain.IhiwLab.class.getName() + ".projects");
            createCache(cm, org.ihiw.management.domain.IhiwLab.class.getName() + ".ihiwUsers");
            createCache(cm, org.ihiw.management.domain.IhiwUser.class.getName());
            createCache(cm, org.ihiw.management.domain.IhiwUser.class.getName() + ".uploads");
            createCache(cm, org.ihiw.management.domain.IhiwUser.class.getName() + ".projectLeaderships");
            createCache(cm, org.ihiw.management.domain.ProjectIhiwLab.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
