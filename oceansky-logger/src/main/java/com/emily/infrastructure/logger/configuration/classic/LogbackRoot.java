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
 * @program: spring-parent
 * @description: 日志组件抽象类
 * @author: Emily
 * @create: 2021/07/08
 */
public class LogbackRoot extends AbstractLogback {
    private final LoggerContext loggerContext;
    private final LoggerProperties properties;

    public LogbackRoot(LoggerProperties properties, LoggerContext loggerContext) {
        this.properties = properties;
        this.loggerContext = loggerContext;
    }

    /**
     * 构建RootLogger对象，需在配置类中主动调用进行初始化
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     */
    @Override
    public Logger getLogger(LogbackProperty property) {
        // 获取logger对象
        Logger logger = loggerContext.getLogger(property.getLoggerName());
        //设置是否向上级打印信息
        logger.setAdditive(false);
        // 设置日志级别
        logger.setLevel(Level.toLevel(properties.getRoot().getLevel().levelStr));
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
        if (properties.getRoot().isConsole()) {
            //移除console控制台appender
            logger.detachAppender(LogbackConsoleAppender.CONSOLE);
            //基于springboot默认初始化的appender name默认大写
            logger.detachAppender(LogbackConsoleAppender.CONSOLE.toUpperCase());
            // 添加控制台appender
            logger.addAppender(new LogbackConsoleAppender(properties, loggerContext).newInstance(logger.getLevel()));
        } else {
            //移除console控制台appender
            logger.detachAppender(LogbackConsoleAppender.CONSOLE);
            //基于springboot默认初始化appender name默认大写
            logger.detachAppender(LogbackConsoleAppender.CONSOLE.toUpperCase());
        }
        return logger;
    }
}
