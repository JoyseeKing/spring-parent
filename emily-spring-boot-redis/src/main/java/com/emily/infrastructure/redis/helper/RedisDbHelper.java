package com.emily.infrastructure.redis.helper;

import com.emily.infrastructure.common.constant.CharacterInfo;
import com.emily.infrastructure.common.enums.AppHttpStatus;
import org.springframework.util.Assert;

/**
 * @Description :  Redis操作帮助类
 * @Author :  Emily
 * @CreateDate :  Created in 2022/8/18 2:05 下午
 */
public class RedisDbHelper {

    /**
     * 获取Redis键key方法 A:B:C
     *
     * @param prefix key的开头
     * @param keys   可以指定多个key
     * @return
     */
    public static String getKey(String prefix, String... keys) {
        Assert.notNull(prefix, AppHttpStatus.ILLEGAL_ARGUMENT.getMessage());
        StringBuffer sb = new StringBuffer(prefix);
        for (int i = 0; i < keys.length; i++) {
            sb.append(CharacterInfo.COLON_EN);
            sb.append(keys[i]);
        }
        return sb.toString();
    }

}
