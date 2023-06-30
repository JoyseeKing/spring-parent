package com.emily.infrastructure.autoconfigure.response;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emily
 * @program: spring-parent
 * @description: 返回值配置文件类
 * @create: 2020/03/25
 */
@ConfigurationProperties(prefix = ResponseWrapperProperties.PREFIX)
public class ResponseWrapperProperties {
    /**
     * 属性配置
     */
    public static final String PREFIX = "spring.emily.response";
    /**
     * 组件开关，默认：true
     */
    private boolean enabled = true;
    /**
     * 开启切面模式包装处理，默认：true
     */
    private boolean enabledAdvice = true;
    /**
     * 开启适配器模式包装处理，默认：false
     */
    private boolean enabledAdapter;
    /**
     * 忽略包装指定URL
     */
    private Set<String> exclude = new HashSet<>() {{
        add("/swagger-resources/**");
        add("/v2/api-docs");
        add("/swagger-ui.html");
        add("/oauth/token");
        add("/error");
    }};

    public boolean isEnabledAdapter() {
        return enabledAdapter;
    }

    public void setEnabledAdapter(boolean enabledAdapter) {
        this.enabledAdapter = enabledAdapter;
    }

    public boolean isEnabledAdvice() {
        return enabledAdvice;
    }

    public void setEnabledAdvice(boolean enabledAdvice) {
        this.enabledAdvice = enabledAdvice;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getExclude() {
        return exclude;
    }

    public void setExclude(Set<String> exclude) {
        this.exclude = exclude;
    }
}
