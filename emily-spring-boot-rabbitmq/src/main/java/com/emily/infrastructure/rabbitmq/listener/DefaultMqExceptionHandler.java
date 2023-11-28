package com.emily.infrastructure.rabbitmq.listener;

import com.emily.infrastructure.common.UUIDUtils;
import com.emily.infrastructure.core.entity.BaseLogger;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.helper.SystemNumberHelper;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.logger.LoggerFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.impl.StrictExceptionHandler;
import org.slf4j.Logger;

import java.time.LocalDateTime;

/**
 * 默认异常处理
 *
 * @author :  Emily
 * @since :  2023/8/22 5:33 PM
 */
public class DefaultMqExceptionHandler extends StrictExceptionHandler {
    private static final Logger logger = LoggerFactory.getModuleLogger(DefaultMqExceptionHandler.class, "api", "request");

    @Override
    public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
        super.handleUnexpectedConnectionDriverException(conn, exception);
        BaseLogger baseLogger = BaseLogger.newBuilder()
                .withSystemNumber(SystemNumberHelper.getSystemNumber())
                .withTraceId(UUIDUtils.randomSimpleUUID())
                .withClientIp(RequestUtils.getClientIp())
                .withServerIp(RequestUtils.getServerIp())
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                .withUrl("RabbitMQ")
                .withBody("An unexpected connection driver error occurred" + " (Exception message: " + exception.getMessage() + ")")
                .build();
        logger.info(JsonUtils.toJSONString(baseLogger));
    }
}
