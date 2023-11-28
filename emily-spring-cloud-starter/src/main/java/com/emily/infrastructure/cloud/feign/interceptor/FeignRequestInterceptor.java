package com.emily.infrastructure.cloud.feign.interceptor;

import com.emily.infrastructure.cloud.feign.context.FeignContextHolder;
import com.emily.infrastructure.core.constant.AttributeInfo;
import com.emily.infrastructure.core.constant.CharacterInfo;
import com.emily.infrastructure.core.constant.HeaderInfo;
import com.emily.infrastructure.core.context.holder.LocalContextHolder;
import com.emily.infrastructure.core.entity.BaseLogger;
import com.emily.infrastructure.core.entity.BaseLoggerBuilder;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.time.LocalDateTime;

/**
 * feign请求日志拦截
 *
 * @author Emily
 * @since 2021/03/31
 */
public class FeignRequestInterceptor implements RequestInterceptor, PriorityOrdered {

    @Override
    public void apply(RequestTemplate template) {
        //请求header设置事务ID
        template.header(HeaderInfo.TRACE_ID, LocalContextHolder.current().getTraceId());
        //封装异步日志信息
        BaseLoggerBuilder builder = BaseLogger.newBuilder()
                //事务唯一编号
                .withTraceId(LocalContextHolder.current().getTraceId())
                //时间
                .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                //请求url
                .withUrl(String.format("%s%s", StringUtils.rightPad(template.feignTarget().url(), 1, CharacterInfo.PATH_SEPARATOR), RegExUtils.replaceFirst(template.url(), CharacterInfo.PATH_SEPARATOR, "")))
                //请求参数
                .withRequestParams(AttributeInfo.HEADERS, template.headers());
        // 将日志信息放入请求对象
        FeignContextHolder.bind(builder);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
