package com.emily.infrastructure.autoconfigure.exception.handler;


import com.emily.infrastructure.common.enums.AppHttpStatus;
import com.emily.infrastructure.common.exception.BasicException;
import com.emily.infrastructure.core.entity.BaseResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Emily
 * @Description: 控制并统一处理异常类
 * @ExceptionHandler标注的方法优先级问题，它会找到异常的最近继承关系，也就是继承关系最浅的注解方法
 * @Version: 1.0
 */
@RestControllerAdvice
public class DefaultGlobalExceptionHandler extends GlobalExceptionCustomizer {
    /**
     * 未知异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.EXCEPTION.getStatus(), e.getMessage());
    }

    /**
     * 运行时异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.EXCEPTION.getStatus(), e.getMessage());
    }

    /**
     * 业务异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BasicException.class)
    public BaseResponse basicException(BasicException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(e.getStatus(), e.getMessage());
    }

    /**
     * 非法代理
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UndeclaredThrowableException.class)
    public BaseResponse undeclaredThrowableException(UndeclaredThrowableException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_PROXY);
    }

    /**
     * 空指针异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public BaseResponse nullPointerExceptionHandler(NullPointerException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_DATA);
    }

    /**
     * 类型转换异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ClassCastException.class)
    public BaseResponse classCastExceptionHandler(ClassCastException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_DATA);
    }

    /**
     * IO异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public BaseResponse ioExceptionHandler(IOException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.EXCEPTION);
    }

    /**
     * 数组越界异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public BaseResponse indexOutOfBoundsException(IndexOutOfBoundsException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_DATA);
    }

    /**
     * API-参数类型不匹配
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TypeMismatchException.class)
    public BaseResponse requestTypeMismatch(TypeMismatchException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_ARGUMENT);
    }

    /**
     * API-缺少参数
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingRequestValueException.class)
    public BaseResponse requestMissingServletRequest(MissingRequestValueException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_ARGUMENT);
    }


    /**
     * API-控制器方法参数Validate异常
     *
     * @throws BindException
     * @throws MethodArgumentNotValidException
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BindException.class, IllegalArgumentException.class, HttpMessageConversionException.class})
    public BaseResponse validModelBindException(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_ARGUMENT);
    }

    /**
     * API-请求method不匹配
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_METHOD);
    }

    /**
     * 数字格式异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NumberFormatException.class)
    public BaseResponse numberFormatException(NumberFormatException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(AppHttpStatus.ILLEGAL_DATA);
    }

}

