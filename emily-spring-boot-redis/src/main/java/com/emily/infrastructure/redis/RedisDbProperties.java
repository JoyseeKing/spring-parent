package com.emily.infrastructure.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis多数据源配置文件
 *
 * @author Emily
 * @since 2021/07/11
 */
@ConfigurationProperties(prefix = RedisDbProperties.PREFIX)
public class RedisDbProperties {
    /**
     * 属性配置前缀
     */
    public static final String PREFIX = "spring.emily.redis";
    /**
     * 是否开启数据源组件, 默认：true
     */
    private boolean enabled = true;
    /**
     * 默认配置标识
     */
    private String defaultConfig;
    /**
     * 是否开启连接校验，默认：false
     */
    private boolean validateConnection = false;
    /**
     * 是否开启共享本地物理连接，默认：true
     */
    private boolean shareNativeConnection = true;
    /**
     * 是否提前初始化连接，默认：false
     */
    private boolean eagerInitialization = false;
    /**
     * 客户端类型
     */
    private RedisProperties.ClientType clientType = RedisProperties.ClientType.LETTUCE;
    /**
     * 多数据源配置
     */
    private Map<String, RedisProperties> config = new HashMap<>();

    public Map<String, RedisProperties> getConfig() {
        return config;
    }

    public RedisProperties.ClientType getClientType() {
        return clientType;
    }

    public void setClientType(RedisProperties.ClientType clientType) {
        this.clientType = clientType;
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public void setConfig(Map<String, RedisProperties> config) {
        this.config = config;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RedisProperties getDefaultDataSource() {
        return this.config.get(this.getDefaultConfig());
    }

    public boolean isValidateConnection() {
        return validateConnection;
    }

    public void setValidateConnection(boolean validateConnection) {
        this.validateConnection = validateConnection;
    }

    public boolean isShareNativeConnection() {
        return shareNativeConnection;
    }

    public void setShareNativeConnection(boolean shareNativeConnection) {
        this.shareNativeConnection = shareNativeConnection;
    }

    public boolean isEagerInitialization() {
        return eagerInitialization;
    }

    public void setEagerInitialization(boolean eagerInitialization) {
        this.eagerInitialization = eagerInitialization;
    }
}
