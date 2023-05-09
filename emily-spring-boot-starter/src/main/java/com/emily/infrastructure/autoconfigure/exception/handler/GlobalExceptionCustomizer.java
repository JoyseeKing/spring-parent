package com.emily.infrastructure.autoconfigure.exception.handler;

import com.emily.infrastructure.common.constant.AttributeInfo;
import com.emily.infrastructure.common.constant.HeaderInfo;
import com.emily.infrastructure.common.date.DatePatternType;
import com.emily.infrastructure.common.entity.BaseLoggerBuilder;
import com.emily.infrastructure.common.exception.BasicException;
import com.emily.infrastructure.common.exception.PrintExceptionInfo;
import com.emily.infrastructure.common.object.JSONUtils;
import com.emily.infrastructure.common.object.UUIDUtils;
import com.emily.infrastructure.common.sensitive.SensitiveUtils;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.context.holder.ThreadContextHolder;
import com.emily.infrastructure.core.helper.RequestHelper;
import com.emily.infrastructure.logger.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * @Description :  异常处理基础类
 * @Author : Emily
 * @CreateDate :  Created in 2022/7/8 1:43 下午
 */
public class GlobalExceptionCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    /**
     * 获取异常堆栈信息并记录到error文件中
     */
    public static void recordErrorMsg(Throwable ex, HttpServletRequest request) {
        String errorMsg = PrintExceptionInfo.printErrorInfo(ex);
        if (ex instanceof BasicException) {
            BasicException systemException = (BasicException) ex;
            errorMsg = MessageFormat.format("业务异常，异常码是【{0}】，异常消息是【{1}】，异常详情{2}", systemException.getStatus(), systemException.getMessage(), errorMsg);
        }
        logger.error(errorMsg);
        //记录错误日志
        recordErrorLogger(request, ex, errorMsg);
    }

    /**
     * 记录错误日志
     *
     * @param request
     * @param errorMsg
     */
    private static void recordErrorLogger(HttpServletRequest request, Throwable ex, String errorMsg) {
        if (Objects.isNull(request)) {
            return;
        }
        if (Objects.nonNull(request.getAttribute(AttributeInfo.STAGE))) {
            return;
        }
        try {
            //事务流水号
            String traceId = request.getHeader(HeaderInfo.TRACE_ID) == null ? UUIDUtils.randomSimpleUUID() : request.getHeader(HeaderInfo.TRACE_ID);

            BaseLoggerBuilder builder = new BaseLoggerBuilder()
                    //系统编号
                    .systemNumber(ThreadContextHolder.current().getSystemNumber())
                    //事务唯一编号
                    .traceId(traceId)
                    //请求URL
                    .url(request.getRequestURI())
                    //客户端IP
                    .clientIp(RequestUtils.getClientIp())
                    //服务端IP
                    .serverIp(RequestUtils.getServerIp())
                    //版本类型
                    .appType(ThreadContextHolder.current().getAppType())
                    //版本号
                    .appVersion(ThreadContextHolder.current().getAppVersion())
                    //触发时间
                    .triggerTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePatternType.YYYY_MM_DD_HH_MM_SS_SSS.getPattern())));
            Map<String, Object> paramMap = null;
            //请求参数
            if (ex instanceof BindException) {
                BindingResult bindingResult = ((BindException) ex).getBindingResult();
                if (Objects.nonNull(bindingResult) && Objects.nonNull(bindingResult.getTarget())) {
                    builder.requestParams(AttributeInfo.HEADERS, RequestHelper.getHeaders(request));
                    builder.requestParams(AttributeInfo.PARAMS, SensitiveUtils.acquire(bindingResult.getTarget()));
                }
            }
            if (CollectionUtils.isEmpty(builder.getRequestParams())) {
                builder.requestParams(RequestHelper.getApiArgs(request));
            }
            //响应体
            builder.body(errorMsg)
                    //耗时(未处理任何逻辑)
                    .spentTime(0L);
            //记录日志到文件
            logger.info(JSONUtils.toJSONString(builder.build()));
        } catch (Exception exception) {
            logger.error(MessageFormat.format("记录错误日志异常：{0}", PrintExceptionInfo.printErrorInfo(exception)));
        } finally {
            //由于获取参数中会初始化上下文，清除防止OOM
            ThreadContextHolder.unbind(true);
        }
    }
}
