package com.emily.infrastructure.datasource.helper;

import com.emily.infrastructure.datasource.DataSourceProperties;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description :  多数据源帮助类
 * @Author :  Emily
 * @CreateDate :  Created in 2022/7/13 7:23 下午
 */
public class DataSourceHelper {

    /**
     * 获取默认数据源
     *
     * @return
     */
    public static Object getDefaultTargetDataSource(DataSourceProperties properties) {
        return getTargetDataSources(properties).get(properties.getDefaultConfig());
    }

    /**
     * 获取合并后的目标数据源配置
     *
     * @return
     */
    public static Map<Object, Object> getTargetDataSources(DataSourceProperties properties) {
        Map<Object, Object> dsMap = new HashMap();
        if (!CollectionUtils.isEmpty(properties.getDruid())) {
            dsMap.putAll(properties.getDruid());
        }
        if (!CollectionUtils.isEmpty(properties.getHikari())) {
            dsMap.putAll(properties.getHikari());
        }
        if (!CollectionUtils.isEmpty(properties.getJndi())) {
            dsMap.putAll(properties.getJndi());
        }
        return Collections.unmodifiableMap(dsMap);
    }
}
