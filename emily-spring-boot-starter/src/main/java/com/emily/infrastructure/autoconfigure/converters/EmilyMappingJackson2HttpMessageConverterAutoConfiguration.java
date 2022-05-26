package com.emily.infrastructure.autoconfigure.converters;

import com.emily.infrastructure.logger.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author Emily
 * @program: spring-parent
 * @description: springboot框架将字典数据类型转换为json，Content-Type默认由 content-type: application/json 更改为：content-type: application/json;charset=UTF-8
 * @create: 2020/10/28
 */
@AutoConfiguration(after = HttpMessageConvertersAutoConfiguration.class)
@EnableConfigurationProperties(Jackson2MessagesProperties.class)
@ConditionalOnProperty(prefix = "spring.emily.jackson2.converter", name = "enable", havingValue = "true", matchIfMissing = true)
public class EmilyMappingJackson2HttpMessageConverterAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(EmilyMappingJackson2HttpMessageConverterAutoConfiguration.class);

    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public EmilyMappingJackson2HttpMessageConverterAutoConfiguration(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @PostConstruct
    public void EmilyMappingJackson2HttpMessageConverterAutoConfiguration() {
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
    }

    @Override
    public void destroy() throws Exception {
        logger.info("<== 【销毁--自动化配置】----响应报文Content-Type编码组件【EmilyMappingJackson2HttpMessageConverterAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("==> 【初始化--自动化配置】----响应报文Content-Type编码组件【EmilyMappingJackson2HttpMessageConverterAutoConfiguration】");
    }
}
