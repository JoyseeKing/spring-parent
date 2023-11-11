package com.emily.infrastructure.redis.common;

import com.emily.infrastructure.common.StringUtils;
import com.emily.infrastructure.common.UUIDUtils;
import com.emily.infrastructure.core.entity.BaseLogger;
import com.emily.infrastructure.core.entity.BaseLoggerBuilder;
import com.emily.infrastructure.core.exception.PrintExceptionInfo;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.helper.SystemNumberHelper;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.logger.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.Collections.singletonList;

/**
 * 基于lua脚本的相关组件
 *
 * @author :  Emily
 * @since :  2023/11/9 11:07 PM
 */
public class LuaScriptTools {
    private static final Logger logger = LoggerFactory.getModuleLogger(LuaScriptTools.class, "api", "request");
    /**
     * 基于lua列表的环形结构实现脚本
     */
    public static String LUA_SCRIPT_CIRCLE;

    /**
     * 基于lua脚本的限流工具
     *
     * @param redisTemplate redis模板工具类
     * @param key           限流键名
     * @param threshold     限流阀值
     * @param expire        限流的时间窗口
     * @return true-访问有效，false-超过阀值
     */
    public static boolean limit(RedisTemplate redisTemplate, String key, int threshold, int expire) {
        RedisScript<Long> script = RedisScript.of(new ClassPathResource("META-INF/scripts/limit.lua"), Long.class);
        // 0：超过阀值 1：访问有效
        Long count = (Long) redisTemplate.execute(script, singletonList(key), threshold, expire);
        return count == 1 ? true : false;
    }

    /**
     * 基于列表（List）的环, 列表永久有效
     *
     * @param redisTemplate redis 模板工具
     * @param key           环的键值
     * @param value         列表值
     * @param threshold     阀值，列表长度，即环上数据个数
     * @return 当前环（列表）长度
     */
    public static long circle(RedisTemplate redisTemplate, String key, Object value, long threshold) {
        return circle(redisTemplate, key, value, threshold, Duration.ZERO);
    }

    /**
     * 基于列表（List）的环
     * 1. 支持一直有效，threshold 设置为<=0或null
     * 2. 支持设置有效时长，动态刷新，interval大于0
     *
     * @param redisTemplate redis 模板工具
     * @param key           环的键值
     * @param value         列表值
     * @param threshold     阀值，列表长度，即环上数据个数
     * @param expire        有效时长, 为null则永久有效
     * @return 当前环（列表）长度
     */
    public static long circle(RedisTemplate redisTemplate, String key, Object value, long threshold, Duration expire) {
        try {
            if (StringUtils.isEmpty(LUA_SCRIPT_CIRCLE)) {
                LUA_SCRIPT_CIRCLE = getLuaScript("META-INF/scripts/list_circle.lua");
            }
            RedisScript<Long> script = RedisScript.of(LUA_SCRIPT_CIRCLE, Long.class);
            if (expire == null) {
                expire = Duration.ZERO;
            }
            return (Long) redisTemplate.execute(script, singletonList(key), value, threshold, expire.getSeconds());
        } catch (Throwable ex) {
            BaseLogger baseLogger = BaseLoggerBuilder.create()
                    .withSystemNumber(SystemNumberHelper.getSystemNumber())
                    .withTraceId(UUIDUtils.randomSimpleUUID())
                    .withClientIp(RequestUtils.getClientIp())
                    .withServerIp(RequestUtils.getServerIp())
                    .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                    .withUrl("Redis")
                    .withRequestParams(key, value)
                    .withRequestParams("threshold", threshold)
                    .withRequestParams("expire", expire.getSeconds())
                    .withBody(PrintExceptionInfo.printErrorInfo(ex.getCause()))
                    .build();
            logger.info(JsonUtils.toJSONString(baseLogger));
            throw ex;
        }
    }

    /**
     * 获取lua脚本
     *
     * @param filePath 脚本路径
     * @return lua字符串脚本
     */
    public static String getLuaScript(String filePath) {
        try {
            return new ClassPathResource(filePath).getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
