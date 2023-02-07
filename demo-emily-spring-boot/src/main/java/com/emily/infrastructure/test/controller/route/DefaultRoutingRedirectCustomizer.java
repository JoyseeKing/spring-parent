package com.emily.infrastructure.test.controller.route;

import com.emily.infrastructure.core.servlet.filter.RoutingRedirectCustomizer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description :  默认路由跳转实现
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/4 1:13 下午
 */
public class DefaultRoutingRedirectCustomizer implements RoutingRedirectCustomizer {
    @Override
    public boolean isRouteRedirect(HttpServletRequest request) {
        return false;
    }

    @Override
    public String resolveSpecifiedLookupPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
