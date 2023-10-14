package com.emily.infrastructure.logger.configuration.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import com.emily.infrastructure.logger.common.PathUtils;
import com.emily.infrastructure.logger.common.StrUtils;
import com.emily.infrastructure.logger.configuration.encoder.LogbackEncoderBuilder;
import com.emily.infrastructure.logger.configuration.filter.LogbackFilterBuilder;
import com.emily.infrastructure.logger.configuration.policy.LogbackRollingPolicyBuilder;
import com.emily.infrastructure.logger.configuration.property.LogbackProperty;
import com.emily.infrastructure.logger.configuration.property.LoggerProperties;
import com.emily.infrastructure.logger.configuration.type.LogbackType;

import java.io.File;
import java.text.MessageFormat;

/**
 * 通过名字和级别设置Appender
 *
 * @author Emily
 * @since : 2020/08/04
 */
public class RollingFileAppenderBuilder extends AbstractAppender {
    /**
     * 属性配置
     */
    private final LogbackProperty property;
    /**
     * 属性配置
     */
    private final LoggerProperties properties;
    /**
     * logger上下文
     */
    private final LoggerContext loggerContext;

    private RollingFileAppenderBuilder(LoggerProperties properties, LoggerContext loggerContext, LogbackProperty property) {
        this.properties = properties;
        this.loggerContext = loggerContext;
        this.property = property;
    }

    /**
     * 获取按照时间归档文件附加器对象
     *
     * @param level 日志级别
     * @return appender
     */
    @Override
    protected Appender<ILoggingEvent> getAppender(Level level) {
        //创建策略对象
        LogbackRollingPolicyBuilder policy = LogbackRollingPolicyBuilder.create(loggerContext, properties.getAppender().getRollingPolicy());
        //日志文件路径
        String loggerPath = this.resolveFilePath(level);
        //这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        //设置文件名，policy激活后才可以从appender获取文件路径
        appender.setFile(loggerPath);
        //设置日志文件归档策略
        appender.setRollingPolicy(policy.build(appender, loggerPath));
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(loggerContext);
        //appender的name属性
        appender.setName(this.resolveName(level));
        //如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true
        appender.setAppend(properties.getAppender().isAppend());
        //如果是 true，日志会被安全的写入文件，即使其他的appender也在向此文件做写入操作，效率低，默认是 false|Support multiple-JVM writing to the same log file
        appender.setPrudent(properties.getAppender().isPrudent());
        //设置过滤器
        appender.addFilter(LogbackFilterBuilder.create(loggerContext).buildLevelFilter(level));
        //设置附加器编码
        appender.setEncoder(LogbackEncoderBuilder.create(loggerContext).buildPatternLayoutEncoder(this.resolveFilePattern()));
        //设置是否将输出流刷新，确保日志信息不丢失，默认：true
        appender.setImmediateFlush(properties.getAppender().isImmediateFlush());
        appender.start();
        return appender;
    }

    /**
     * 获取文件路径
     *
     * @param level 日志级别
     * @return 日志文件路径
     */
    @Override
    protected String resolveFilePath(Level level) {
        //基础相对路径
        String basePath = properties.getAppender().getPath();
        //文件路径
        String filePath = property.getFilePath();
        //日志级别
        String levelStr = level.levelStr.toLowerCase();
        // 基础路径
        String loggerPath = StrUtils.join(basePath, filePath, File.separator);
        //基础日志、分组日志
        if (LogbackType.ROOT.equals(property.getLogbackType()) || LogbackType.GROUP.equals(property.getLogbackType())) {
            loggerPath = StrUtils.join(loggerPath, levelStr, File.separator, levelStr);
        }
        //分模块日志
        else if (LogbackType.MODULE.equals(property.getLogbackType())) {
            loggerPath = StrUtils.join(loggerPath, property.getFileName());
        } else {
            throw new UnsupportedOperationException("Unsupported log type");
        }
        return StrUtils.substVars(loggerContext, loggerPath, ".log");
    }

    /**
     * 获取日志输出格式
     *
     * @return 日志格式
     */
    @Override
    protected String resolveFilePattern() {
        //基础日志
        if (LogbackType.ROOT.equals(property.getLogbackType())) {
            return properties.getRoot().getPattern();
        }
        //分组
        if (LogbackType.GROUP.equals(property.getLogbackType())) {
            return properties.getGroup().getPattern();
        }
        //分模块
        return properties.getModule().getPattern();
    }

    /**
     * 日志级别
     * 拼接规则：分组.路径.文件名.日志级别
     *
     * @param level 日志级别
     * @return appender name值
     */
    @Override
    protected String resolveName(Level level) {
        String fileName = property.getFileName();
        if (StrUtils.isEmpty(fileName)) {
            fileName = level.levelStr.toLowerCase();
        }
        //拼装appender name
        return MessageFormat.format("{0}{1}.{2}.{3}", property.getLogbackType(), property.getFilePath(), fileName, level.levelStr.toLowerCase()).replace(PathUtils.SLASH, PathUtils.DOT);
    }

    public static RollingFileAppenderBuilder create(LoggerProperties properties, LoggerContext loggerContext, LogbackProperty property) {
        return new RollingFileAppenderBuilder(properties, loggerContext, property);
    }
}
