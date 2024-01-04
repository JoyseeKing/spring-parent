package com.emily.infrastructure.logger.configuration.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.emily.infrastructure.logger.configuration.encoder.LogbackEncoderBuilder;
import com.emily.infrastructure.logger.configuration.filter.LogbackFilterBuilder;
import com.emily.infrastructure.logger.configuration.property.LoggerConfig;

/**
 * 通过名字和级别设置Appender
 *
 * @author Emily
 * @since : 2020/08/04
 */
public class LogbackConsoleAppender extends AbstractAppender {
    /**
     * 控制台appender name
     * 必须小写，否则会出现多个控制台appender
     */
    public static final String CONSOLE = "console";
    /**
     * 属性配置
     */
    private final LoggerConfig config;
    /**
     * 属性配置
     */
    private final LoggerContext lc;

    private LogbackConsoleAppender(LoggerConfig config, LoggerContext lc) {
        this.config = config;
        this.lc = lc;
    }

    /**
     * 控制台打印appender
     *
     * @param level 日志级别
     * @return consul appender
     */
    @Override
    protected Appender<ILoggingEvent> getAppender(Level level) {
        //这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(lc);
        //appender的name属性
        appender.setName(this.resolveName(level));
        //添加过滤器
        appender.addFilter(LogbackFilterBuilder.create(lc).buildThresholdLevelFilter(level));
        //设置编码
        appender.setEncoder(LogbackEncoderBuilder.create(lc).buildPatternLayoutEncoder(this.resolveFilePattern()));
        //设置是否将输出流刷新，确保日志信息不丢失，默认：true
        appender.setImmediateFlush(true);
        //ANSI color codes支持，默认：false；请注意，基于Unix的操作系统（如Linux和Mac OS X）默认支持ANSI颜色代码。
        appender.setWithJansi(config.getRoot().isWithJansi());
        appender.start();
        return appender;

    }

    @Override
    protected String resolveFilePath(Level level) {
        return null;
    }

    @Override
    protected String resolveFilePattern() {
        return config.getRoot().getConsolePattern();
    }

    @Override
    protected String resolveName(Level level) {
        return CONSOLE;
    }

    public static LogbackConsoleAppender create(LoggerConfig config, LoggerContext lc) {
        return new LogbackConsoleAppender(config, lc);
    }
}
