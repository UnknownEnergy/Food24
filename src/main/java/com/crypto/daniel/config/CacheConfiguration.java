package com.crypto.daniel.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.crypto.daniel.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.crypto.daniel.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyGroup.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyGroup.class.getName() + ".groceryLists", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyGroup.class.getName() + ".familyMembers", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.GroceryList.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.GroceryList.class.getName() + ".storeItems", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.StoreItem.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.StoreItem.class.getName() + ".groceryLists", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.StoreItemInstance.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Store.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Store.class.getName() + ".storeItemInstances", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Location.class.getName() + ".familyMembers", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.Location.class.getName() + ".stores", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyMember.class.getName(), jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyMember.class.getName() + ".groceryLists", jcacheConfiguration);
            cm.createCache(com.crypto.daniel.domain.FamilyMember.class.getName() + ".familyGroups", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
