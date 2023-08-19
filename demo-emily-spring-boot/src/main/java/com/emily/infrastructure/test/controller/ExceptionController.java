package com.emily.infrastructure.test.controller;

import com.emily.infrastructure.core.exception.BusinessException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: spring-parent
 *  异常控制器
 * @author Emily
 * @since 2021/08/21
 */
@RestController
@RequestMapping("/api/exception")
public class ExceptionController {

    @GetMapping("test1")
    public void exception() {
        throw new IllegalArgumentException("非法参数测试");
    }

    @GetMapping("basic")
    public void assert1() {
        throw new BusinessException(100,"业务异常");
    }
}
