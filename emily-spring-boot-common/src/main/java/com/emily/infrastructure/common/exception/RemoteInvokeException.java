package com.emily.infrastructure.common.exception;

import com.emily.infrastructure.common.type.HttpStatusType;

/**
 * @program: spring-parent
 * @description: 调用第三方接口异常
 * @author: Emily
 * @create: 2021/10/12
 */
public class RemoteInvokeException extends BasicException {
    public RemoteInvokeException() {
        super(HttpStatusType.ILLEGAL_ACCESS);
    }

    public RemoteInvokeException(HttpStatusType httpStatus) {
        super(httpStatus);
    }

    public RemoteInvokeException(int status, String errorMessage) {
        super(status, errorMessage);
    }

    public RemoteInvokeException(int status, String errorMessage, boolean error) {
        super(status, errorMessage, error);
    }
}
