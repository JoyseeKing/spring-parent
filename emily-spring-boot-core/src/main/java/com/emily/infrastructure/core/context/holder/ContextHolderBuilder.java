package com.emily.infrastructure.core.context.holder;

import com.emily.infrastructure.common.UUIDUtils;
import com.emily.infrastructure.core.constant.AttributeInfo;
import com.emily.infrastructure.core.constant.HeaderInfo;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.helper.SystemNumberHelper;
import com.emily.infrastructure.language.convert.LanguageType;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

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
    private LanguageType languageType;
    /**
     * 开启时间
     */
    private Instant startTime;
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

    public ContextHolderBuilder withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public ContextHolderBuilder withSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
        return this;
    }

    public ContextHolderBuilder withLanguageType(LanguageType languageType) {
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

    public ContextHolder build() {
        ContextHolder holder = new ContextHolder();
        //servlet请求开始时间
        holder.setStartTime(Instant.now());
        //系统编号
        holder.setSystemNumber(SystemNumberHelper.getSystemNumber());
        //客户端IP
        holder.setClientIp(RequestUtils.getClientIp());
        //服务端IP
        holder.setServerIp(RequestUtils.getServerIp());
        //servlet上下文
        holder.setServlet(RequestUtils.isServlet());
        //版本类型，com.emily.android
        holder.setAppType(RequestUtils.isServlet() ? RequestUtils.getHeader(HeaderInfo.APP_TYPE) : null);
        //版本号，4.1.4
        holder.setAppVersion(RequestUtils.isServlet() ? RequestUtils.getHeader(HeaderInfo.APP_VERSION) : null);
        //语言
        holder.setLanguageType(RequestUtils.isServlet() ? LanguageType.getByCode(RequestUtils.getHeader(HeaderInfo.LANGUAGE)) : LanguageType.ZH_CN);
        //事务流水号
        holder.setTraceId(RequestUtils.isServlet() ? StringUtils.defaultString(RequestUtils.getHeader(HeaderInfo.TRACE_ID), UUIDUtils.randomSimpleUUID()) : UUIDUtils.randomSimpleUUID());
        //设置当前请求阶段标识
        if (RequestUtils.isServlet()) {
            RequestUtils.getRequest().setAttribute(AttributeInfo.STAGE, StageType.REQUEST);
        }
        return holder;
    }
}
