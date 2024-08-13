package com.emily.infrastructure.core.context.holder;

import com.emily.infrastructure.core.helper.SystemNumberHelper;

import java.time.Instant;
import java.util.Objects;

/**
 * 上下文实体类建造器
 *
 * @author Emily
 * @since Created in 2023/4/22 3:51 PM
 */
public class ContextHolderBuilder {
    /**
     * 事务唯一编号
     */
    private String traceId;
    /**
     * 系统编号|标识
     */
    private String systemNumber;
    /**
     * 语言
     */
    private String languageType;
    /**
     * 开启时间
     */
    private Instant startTime;
    /**
     * API接口耗时
     */
    private long spentTime;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 服务端IP
     */
    private String serverIp;
    /**
     * 版本类型，com.emily.android
     */
    private String appType;
    /**
     * 版本号，4.1.4
     */
    private String appVersion;
    /**
     * (逻辑)是否servlet容器上下文，默认：false
     */
    private boolean servlet;
    /**
     * 当前上下文所处阶段标识
     */
    private ServletStage servletStage;

    public ContextHolderBuilder withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public ContextHolderBuilder withSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
        return this;
    }

    public ContextHolderBuilder withLanguageType(String languageType) {
        this.languageType = languageType;
        return this;
    }

    public ContextHolderBuilder withStartTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public ContextHolderBuilder withClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public ContextHolderBuilder withServerIp(String serverIp) {
        this.serverIp = serverIp;
        return this;
    }

    public ContextHolderBuilder withAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public ContextHolderBuilder withAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public ContextHolderBuilder withServlet(boolean servlet) {
        this.servlet = servlet;
        return this;
    }

    public ContextHolderBuilder withServletStage(ServletStage servletStage) {
        this.servletStage = servletStage;
        return this;
    }

    public ContextHolderBuilder withSpentTime(long spentTime) {
        this.spentTime = spentTime;
        return this;
    }

    public ContextHolder build() {
        ContextHolder holder = new ContextHolder();
        //事务流水号
        holder.setTraceId(traceId);
        //系统编号
        holder.setSystemNumber(Objects.isNull(systemNumber) ? SystemNumberHelper.getSystemNumber() : systemNumber);
        //servlet上下文
        holder.setServlet(servlet);
        //语言
        holder.setLanguageType(languageType);
        //版本类型，com.emily.android
        holder.setAppType(appType);
        //版本号，4.1.4
        holder.setAppVersion(appVersion);
        //servlet请求开始时间
        holder.setStartTime(Objects.isNull(startTime) ? Instant.now() : startTime);
        //API耗时
        holder.setSpentTime(spentTime);
        //客户端IP
        holder.setClientIp(clientIp);
        //服务端IP
        holder.setServerIp(serverIp);
        //设置当前请求阶段标识
        holder.setServletStage(Objects.isNull(servletStage) ? ServletStage.OTHER : servletStage);
        return holder;
    }
}
