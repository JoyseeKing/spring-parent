package com.emily.infrastructure.logger.configuration.classic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.emily.infrastructure.logger.configuration.appender.AbstractAppender;
import com.emily.infrastructure.logger.configuration.appender.LogbackAsyncAppender;
import com.emily.infrastructure.logger.configuration.appender.LogbackConsoleAppender;
import com.emily.infrastructure.logger.configuration.appender.LogbackRollingFileAppender;
import com.emily.infrastructure.logger.configuration.property.LogbackProperty;
import com.emily.infrastructure.logger.configuration.property.LoggerProperties;

/**
 * 分组记录日志
 *
 * @author : Emily
 * @since : 2021/12/12
 */
public class LogbackGroup extends AbstractLogback {
    private final LoggerProperties properties;
    private final LoggerContext loggerContext;

    public LogbackGroup(LoggerProperties properties, LoggerContext loggerContext) {
        this.properties = properties;
        this.loggerContext = loggerContext;
    }

    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF &gt; ERROR &gt; WARN &gt; INFO &gt; DEBUG &gt; TRACE &gt;ALL
     *
     * @param property 上下文属性传递类
     * @return 日志对象
     */
    @Override
    public Logger getLogger(LogbackProperty property) {
        // 获取logger对象
        Logger logger = loggerContext.getLogger(property.getLoggerName());
        // 设置是否向上级打印信息
        logger.setAdditive(false);
        // 设置日志级别
        logger.setLevel(Level.toLevel(properties.getGroup().getLevel().levelStr));
        // appender对象
        AbstractAppender fileAppender = new LogbackRollingFileAppender(properties, loggerContext, property);
        // 是否开启异步日志
        if (properties.getAppender().getAsync().isEnabled()) {
            //异步appender
            LogbackAsyncAppender asyncAppender = new LogbackAsyncAppender(properties, loggerContext);
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(asyncAppender.getAppender(fileAppender.newInstance(Level.ERROR)));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(asyncAppender.getAppender(fileAppender.newInstance(Level.WARN)));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(asyncAppender.getAppender(fileAppender.newInstance(Level.INFO)));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(asyncAppender.getAppender(fileAppender.newInstance(Level.DEBUG)));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(asyncAppender.getAppender(fileAppender.newInstance(Level.TRACE)));
            }
        } else {
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(fileAppender.newInstance(Level.ERROR));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(fileAppender.newInstance(Level.WARN));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(fileAppender.newInstance(Level.INFO));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(fileAppender.newInstance(Level.DEBUG));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(fileAppender.newInstance(Level.TRACE));
            }
        }
        if (properties.getGroup().isConsole()) {
            // 添加控制台appender
            logger.addAppender(new LogbackConsoleAppender(properties, loggerContext).newInstance(logger.getLevel()));
        }

        return logger;
    }
}
