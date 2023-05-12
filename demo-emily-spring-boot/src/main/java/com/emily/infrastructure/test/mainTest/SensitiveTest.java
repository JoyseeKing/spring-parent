package com.emily.infrastructure.test.mainTest;

import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.sensitive.SensitiveUtils;
import com.emily.infrastructure.test.po.json.JsonRequest;

/**
 * @Description :  脱敏工具类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/4/17 5:07 PM
 */
public class SensitiveTest {
    public static void main(String[] args) {
        JsonRequest request = new JsonRequest();
        request.setUsername("田雨橙");
        request.setPassword("123456");
        request.setFieldKey("email");
        request.setFieldValue("1383612596@qq.com");
        System.out.println(JsonUtils.toJSONPrettyString(SensitiveUtils.acquire(request)));

    }
}
