package com.emily.infrastructure.redis;

import com.emily.infrastructure.redis.connection.JedisDbConnectionConfiguration;
import com.emily.infrastructure.redis.connection.LettuceDbConnectionConfiguration;
import com.emily.infrastructure.redis.connection.PropertiesRedisDbConnectionDetails;
import com.emily.infrastructure.redis.utils.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Objects;

import static com.emily.infrastructure.redis.common.RedisBeanNames.*;
import static com.emily.infrastructure.redis.common.SerializationUtils.jackson2JsonRedisSerializer;
import static com.emily.infrastructure.redis.common.SerializationUtils.stringSerializer;

/**
 * Redis多数据源配置，参考源码：LettuceConnectionConfiguration
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}
 *
 * @author Emily
 * @since 2021/07/11
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(before = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisDbProperties.class)
@ConditionalOnProperty(prefix = RedisDbProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({LettuceDbConnectionConfiguration.class, JedisDbConnectionConfiguration.class})
public class RedisDbAutoConfiguration implements InitializingBean, DisposableBean {

    private final RedisDbProperties properties;

    public RedisDbAutoConfiguration(DefaultListableBeanFactory defaultListableBeanFactory, RedisDbProperties properties) {
        BeanFactoryUtils.setDefaultListableBeanFactory(defaultListableBeanFactory);
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionDetails.class)
    PropertiesRedisDbConnectionDetails redisConnectionDetails() {
        String defaultConfig = Objects.requireNonNull(properties.getDefaultConfig(), "Redis默认标识不可为空");
        PropertiesRedisDbConnectionDetails redisConnectionDetails = null;
        for (Map.Entry<String, RedisProperties> entry : properties.getConfig().entrySet()) {
            String key = entry.getKey();
            RedisProperties redisProperties = entry.getValue();
            PropertiesRedisDbConnectionDetails propertiesRedisDbConnectionDetails = new PropertiesRedisDbConnectionDetails(redisProperties);
            if (defaultConfig.equals(key)) {
                redisConnectionDetails = propertiesRedisDbConnectionDetails;
            } else {
                BeanFactoryUtils.registerSingleton(join(key, REDIS_CONNECT_DETAILS), propertiesRedisDbConnectionDetails);
            }
        }
        return redisConnectionDetails;
    }

    @Bean
    @ConditionalOnMissingBean(name = DEFAULT_REDIS_TEMPLATE)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        String defaultConfig = Objects.requireNonNull(properties.getDefaultConfig(), "Redis默认标识不可为空");
        RedisTemplate<Object, Object> redisTemplate = null;
        for (Map.Entry<String, RedisProperties> entry : properties.getConfig().entrySet()) {
            String key = entry.getKey();
            RedisTemplate<Object, Object> template = new RedisTemplate<>();
            template.setKeySerializer(stringSerializer());
            template.setValueSerializer(jackson2JsonRedisSerializer());
            template.setHashKeySerializer(stringSerializer());
            template.setHashValueSerializer(jackson2JsonRedisSerializer());
            if (defaultConfig.equals(key)) {
                template.setConnectionFactory(redisConnectionFactory);
                redisTemplate = template;
            } else {
                template.setConnectionFactory(BeanFactoryUtils.getBean(join(key, REDIS_CONNECTION_FACTORY), RedisConnectionFactory.class));
                template.afterPropertiesSet();
                BeanFactoryUtils.registerSingleton(join(key, REDIS_TEMPLATE), template);
            }
        }

        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = DEFAULT_STRING_REDIS_TEMPLATE)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        String defaultConfig = Objects.requireNonNull(properties.getDefaultConfig(), "Redis默认标识不可为空");
        StringRedisTemplate stringRedisTemplate = null;
        for (Map.Entry<String, RedisProperties> entry : properties.getConfig().entrySet()) {
            String key = entry.getKey();
            StringRedisTemplate template = new StringRedisTemplate();
            template.setKeySerializer(stringSerializer());
            template.setValueSerializer(stringSerializer());
            template.setHashKeySerializer(stringSerializer());
            template.setHashValueSerializer(stringSerializer());
            if (defaultConfig.equals(key)) {
                template.setConnectionFactory(redisConnectionFactory);
                stringRedisTemplate = template;
            } else {
                template.setConnectionFactory(BeanFactoryUtils.getBean(join(key, REDIS_CONNECTION_FACTORY), RedisConnectionFactory.class));
                template.afterPropertiesSet();
                BeanFactoryUtils.registerSingleton(join(key, STRING_REDIS_TEMPLATE), template);
            }
        }

        return stringRedisTemplate;
    }


    @Override
    public void destroy() {
        LogHolder.LOG.info("<== 【销毁--自动化配置】----Redis数据库多数据源组件【RedisDbAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        LogHolder.LOG.info("==> 【初始化--自动化配置】----Redis数据库多数据源组件【RedisDbAutoConfiguration】");
    }

    private static class LogHolder {
        private static final Logger LOG = LoggerFactory.getLogger(RedisDbAutoConfiguration.class);
    }
}
