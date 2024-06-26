package com.emily.infrastructure.redis.common;

import org.springframework.util.Assert;

/**
 * Redis操作帮助类
 *
 * @author Emily
 * @since Created in 2022/8/18 2:05 下午
 */
public class RedisCommonKeys {
    private static final String COLON_EN = ":";

    /**
     * 获取Redis键key方法 A:B:C
     *
     * @param prefix key的开头
     * @param keys   可以指定多个key
     * @return redis建值
     */
    public static String getKey(String prefix, String... keys) {
        Assert.notNull(prefix, "非法参数");
        StringBuilder sb = new StringBuilder(prefix);
        for (String key : keys) {
            sb.append(COLON_EN);
            sb.append(key);
        }
        return sb.toString();
    }

}
