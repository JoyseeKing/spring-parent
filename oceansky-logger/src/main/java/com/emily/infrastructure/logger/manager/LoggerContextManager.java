package com.emily.infrastructure.logger.manager;

import ch.qos.logback.classic.LoggerContext;
import com.emily.infrastructure.logger.configuration.context.LogbackContext;
import com.emily.infrastructure.logger.configuration.property.LoggerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description :  日志初始化管理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/7/2 11:16 AM
 */
public class LoggerContextManager {
    private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static final Logger logger = LoggerFactory.getLogger(LoggerContextManager.class);
    /**
     * logback sdk context
     */
    private static LogbackContext context;
    /**
     * 是否已经初始化，默认：false
     */
    private static boolean initialized = false;

    /**
     * 日志组件SDK初始化
     *
     * @param properties 日志属性配置
     */
    public static void init(LoggerProperties properties) {
        if (!properties.isEnabled()) {
            return;
        }
        if (initialized) {
            context.stopAndReset();
        }
        // 初始化日志上下文
        context = new LogbackContext(properties, LOGGER_CONTEXT);
        if (initialized) {
            logger.warn("Log sdk initialized");
            logger.warn("It has already been initialized,please do not repeatedly initialize the log sdk.");
        } else {
            logger.info("Log sdk initialized");
        }
        // 设置为已初始化
        initialized = true;
    }

    public static LogbackContext getContext() {
        if (!initialized) {
            throw new IllegalStateException("Log sdk not initialized");
        }
        return context;
    }
}
