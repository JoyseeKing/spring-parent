package com.emily.infrastructure.autoconfigure.request.interceptor;

import com.emily.infrastructure.common.constant.AopOrderInfo;
import com.emily.infrastructure.common.constant.AttributeInfo;
import com.emily.infrastructure.common.constant.CharacterInfo;
import com.emily.infrastructure.common.date.DatePatternType;
import com.emily.infrastructure.common.entity.BaseLoggerBuilder;
import com.emily.infrastructure.common.exception.BasicException;
import com.emily.infrastructure.common.exception.HttpStatusType;
import com.emily.infrastructure.common.exception.PrintExceptionInfo;
import com.emily.infrastructure.common.i18n.I18nUtils;
import com.emily.infrastructure.common.sensitive.SensitiveUtils;
import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.core.context.holder.ThreadContextHolder;
import com.emily.infrastructure.core.helper.RequestHelper;
import com.emily.infrastructure.core.helper.ThreadPoolHelper;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.logger.LoggerFactory;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * @author Emily
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * @Version: 1.0
 */
public class DefaultRequestMethodInterceptor implements RequestCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRequestMethodInterceptor.class);

    /**
     * 拦截接口日志
     *
     * @param invocation 接口方法切面连接点
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //封装异步日志信息
        BaseLoggerBuilder builder = new BaseLoggerBuilder();
        try {
            //系统编号
            builder.systemNumber(ThreadContextHolder.current().getSystemNumber())
                    //事务唯一编号
                    .traceId(ThreadContextHolder.current().getTraceId())
                    //时间
                    .triggerTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePatternType.YYYY_MM_DD_HH_MM_SS_SSS.getPattern())))
                    //请求url
                    .url(StringUtils.substringBefore(String.valueOf(RequestUtils.getRequest().getRequestURL()), CharacterInfo.ASK_SIGN_EN))
                    //请求参数
                    .requestParams(RequestHelper.getApiArgs(invocation));
            //调用真实的action方法
            Object response = invocation.proceed();
            if (Objects.nonNull(response) && response instanceof ResponseEntity) {
                Object responseBody = ((ResponseEntity) response).getBody();
                //404 Not Fund
                handleNotFund(response, builder);
                //设置响应结果
                builder.body(SensitiveUtils.acquire(responseBody));
            } else {
                //设置响应结果
                builder.body(SensitiveUtils.acquire(response));
            }
            return I18nUtils.acquire(response, ThreadContextHolder.current().getLanguageType());
        } catch (Exception ex) {
            if (ex instanceof BasicException) {
                BasicException exception = (BasicException) ex;
                //响应码
                builder.status(exception.getStatus())
                        //响应描述
                        .message(exception.getMessage())
                        //异常响应体
                        .body(StringUtils.join("【statusCode】", exception.getStatus(), ", 【errorMessage】", exception.getMessage()));
            } else {
                //响应码
                builder.status(HttpStatusType.EXCEPTION.getStatus())
                        //响应描述
                        .message(HttpStatusType.EXCEPTION.getMessage())
                        //异常响应体
                        .body(PrintExceptionInfo.printErrorInfo(ex));
            }
            throw ex;
        } finally {
            //客户端IP
            builder.clientIp(ThreadContextHolder.current().getClientIp())
                    //服务端IP
                    .serverIp(ThreadContextHolder.current().getServerIp())
                    //版本类型
                    .appType(ThreadContextHolder.current().getAppType())
                    //版本号
                    .appVersion(ThreadContextHolder.current().getAppVersion())
                    //耗时
                    .spentTime(System.currentTimeMillis() - ThreadContextHolder.current().getStartTime())
                    //时间
                    .triggerTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePatternType.YYYY_MM_DD_HH_MM_SS_SSS.getPattern())));
            //异步记录接口响应信息
            ThreadPoolHelper.defaultThreadPoolTaskExecutor().submit(() -> logger.info(JsonUtils.toJSONString(builder.build())));
            //移除线程上下文数据
            ThreadContextHolder.unbind(true);
            //设置耗时
            RequestUtils.getRequest().setAttribute(AttributeInfo.SPENT_TIME, builder.getSpentTime());
        }

    }

    /**
     * 404 Not Fund接口处理
     *
     * @param result
     * @param builder
     */
    private void handleNotFund(Object result, BaseLoggerBuilder builder) {
        int status = ((ResponseEntity) result).getStatusCodeValue();
        if (status == HttpStatus.NOT_FOUND.value()) {
            Object resultBody = ((ResponseEntity) result).getBody();
            Map<String, Object> dataMap = JsonUtils.toJavaBean(JsonUtils.toJSONString(resultBody), Map.class);
            builder.url(dataMap.get("path").toString())
                    .status(status)
                    .message(dataMap.get("error").toString());
        }
    }

    @Override
    public int getOrder() {
        return AopOrderInfo.REQUEST_INTERCEPTOR;
    }
}
