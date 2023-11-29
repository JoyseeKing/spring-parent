package com.emily.infrastructure.test.controller;

import com.emily.infrastructure.core.helper.RequestUtils;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.sensitive.annotation.JsonSimField;
import com.emily.infrastructure.sensitive.SensitiveType;
import com.emily.infrastructure.test.po.Job;
import com.emily.infrastructure.test.po.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Emily
 * @program: spring-parent
 * 参数控制器
 * @since 2021/10/30
 */
@Validated
@RestController
@RequestMapping("api/param")
public class ParamController {
    @GetMapping("test")
    public String test() {
        String header = RequestUtils.getHeader("tesT");
        return header;
    }

    @PostMapping("test1")
    public Job test1(@Validated @RequestBody Job job) {
        return job;
    }

    @PostMapping("postList")
    public int postList(@RequestBody List<User> list) {
        System.out.println(JsonUtils.toJSONPrettyString(list));
        return 0;
    }

    @PostMapping("postArray")
    public int postList(@RequestBody User[] list) {
        System.out.println(JsonUtils.toJSONPrettyString(list));
        return 0;
    }

    @GetMapping("rest/{code}/{name}")
    public String restTest(@PathVariable("code") String code, @PathVariable("name") @NotNull String name) {
        return "s";
    }

    @GetMapping("getParam")
    public String getParam(@NotEmpty(message = "用户名不可为空") @JsonSimField(SensitiveType.USERNAME) String username) {
        return username;
    }

    @PostMapping("getBody")
    public String getParam(@Validated @RequestBody Job job) {
        return "sdf";
    }

    @PostMapping("validParam")
    public String validParam(@Validated @NotEmpty(message = "不可为空") String username) {
        return username;
    }
}
