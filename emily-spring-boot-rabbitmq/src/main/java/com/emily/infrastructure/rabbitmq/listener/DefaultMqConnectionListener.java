package com.emily.infrastructure.rabbitmq.listener;

import com.emily.infrastructure.common.UUIDUtils;
import com.emily.infrastructure.core.entity.BaseLogger;
import com.emily.infrastructure.core.exception.PrintExceptionInfo;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.helper.SystemNumberHelper;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.logger.LoggerFactory;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

import java.time.LocalDateTime;

/**
 * rabbit连接监听器
 *
 * @author :  Emily
 * @since :  2023/8/23 9:45 AM
 */
public class DefaultMqConnectionListener implements ConnectionListener {

    private static final Logger logger = LoggerFactory.getModuleLogger(DefaultMqConnectionListener.class, "api", "request");
    private final CachingConnectionFactory connectionFactory;

    public DefaultMqConnectionListener(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Called when a new connection is established.
     *
     * @param connection the connection.
     */
    @Override
    public void onCreate(Connection connection) {
        BaseLogger baseLogger = BaseLogger.newBuilder()
                .withSystemNumber(SystemNumberHelper.getSystemNumber())
                .withTraceId(UUIDUtils.randomSimpleUUID())
                .withClientIp(RequestUtils.getClientIp())
                .withServerIp(RequestUtils.getServerIp())
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                .withUrl("RabbitMQ")
                .withBody("Created new connection [Called when a new connection is established]: " + connectionFactory.toString() + "/" + connection)
                .build();
        logger.info(JsonUtils.toJSONString(baseLogger));
    }

    /**
     * Called when a connection is closed.
     *
     * @param connection the connection.
     */
    @Override
    public void onClose(Connection connection) {
        ConnectionListener.super.onClose(connection);
        BaseLogger baseLogger = BaseLogger.newBuilder()
                .withSystemNumber(SystemNumberHelper.getSystemNumber())
                .withTraceId(UUIDUtils.randomSimpleUUID())
                .withClientIp(RequestUtils.getClientIp())
                .withServerIp(RequestUtils.getServerIp())
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                .withUrl("RabbitMQ")
                .withBody("Close [Called when a connection is closed]: " + connection)
                .build();
        logger.info(JsonUtils.toJSONString(baseLogger));
    }

    /**
     * Called when a connection is force closed.
     *
     * @param signal the shut down signal.
     */
    @Override
    public void onShutDown(ShutdownSignalException signal) {
        ConnectionListener.super.onShutDown(signal);
        BaseLogger baseLogger = BaseLogger.newBuilder()
                .withSystemNumber(SystemNumberHelper.getSystemNumber())
                .withTraceId(UUIDUtils.randomSimpleUUID())
                .withClientIp(RequestUtils.getClientIp())
                .withServerIp(RequestUtils.getServerIp())
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                .withUrl("RabbitMQ")
                .withBody("ShutDown [Called when a connection is force closed] " + signal.getMessage())
                .build();
        logger.info(JsonUtils.toJSONString(baseLogger));
    }

    /**
     * Called when a connection couldn't be established.
     *
     * @param exception the exception thrown.
     */
    @Override
    public void onFailed(Exception exception) {
        ConnectionListener.super.onFailed(exception);
        BaseLogger baseLogger = BaseLogger.newBuilder()
                .withSystemNumber(SystemNumberHelper.getSystemNumber())
                .withTraceId(UUIDUtils.randomSimpleUUID())
                .withClientIp(RequestUtils.getClientIp())
                .withServerIp(RequestUtils.getServerIp())
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                .withUrl("RabbitMQ")
                .withBody("Failed [Called when a connection couldn't be established] " + PrintExceptionInfo.printErrorInfo(exception))
                .build();
        logger.info(JsonUtils.toJSONString(baseLogger));
    }
}
