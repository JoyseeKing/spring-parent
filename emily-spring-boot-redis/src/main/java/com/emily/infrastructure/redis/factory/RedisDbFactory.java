package com.emily.infrastructure.redis.factory;

import com.emily.infrastructure.redis.RedisDbProperties;
import com.emily.infrastructure.redis.utils.IocUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.emily.infrastructure.redis.common.RedisBeanNames.*;

/**
 * Redis数据源
 *
 * @author Emily
 * @since 2021/07/11
 */
public class RedisDbFactory {

    /**
     * 获取Redis默认字符串模板
     *
     * @return redis操作对象
     */
    public static StringRedisTemplate getStringRedisTemplate() {
        return getStringRedisTemplate(null);
    }

    /**
     * 获取Redis模板对戏
     *
     * @param key 数据源标识
     * @return redis操作对象
     */
    public static StringRedisTemplate getStringRedisTemplate(String key) {
        if (key == null || key.isBlank() || IocUtils.getBean(RedisDbProperties.class).getDefaultConfig().equals(key)) {
            return IocUtils.getBean(DEFAULT_STRING_REDIS_TEMPLATE, StringRedisTemplate.class);
        } else {
            return IocUtils.getBean(join(key, STRING_REDIS_TEMPLATE), StringRedisTemplate.class);
        }
    }

    /**
     * 获取Redis默认字符串模板
     *
     * @return redis操作对象
     */
    public static RedisTemplate getRedisTemplate() {
        return getRedisTemplate(null);
    }

    /**
     * 获取Redis模板对戏
     *
     * @param key 数据源标识
     * @return redis操作模板对象
     */
    public static RedisTemplate getRedisTemplate(String key) {
        if (key == null || key.isBlank() || IocUtils.getBean(RedisDbProperties.class).getDefaultConfig().equals(key)) {
            return IocUtils.getBean(DEFAULT_REDIS_TEMPLATE, RedisTemplate.class);
        } else {
            return IocUtils.getBean(join(key, REDIS_TEMPLATE), RedisTemplate.class);
        }
    }
}
