package cn.cutepikachu.datawisemaster.exception;

import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // https://github.com/YunaiV/ruoyi-vue-pro/blob/master/yudao-framework/yudao-spring-boot-starter-web/src/main/java/cn/iocoder/yudao/framework/web/core/handler/GlobalExceptionHandler.java
    // https://github.com/xiaonuobase/Snowy/blob/master/snowy-web-app/src/main/java/vip/xiaonuo/core/handler/GlobalExceptionUtil.java

    /**
     * 参数传递格式不支持异常
     * 415
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public BaseResponse<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "参数格式错误");
    }

    /**
     * 参数传递格式不支持异常
     * 415
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public BaseResponse<?> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        log.error("[HttpMediaTypeNotSupportedException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "参数格式错误");
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     * 415
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public BaseResponse<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("[MissingServletRequestParameterException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "请求参数缺失");
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public BaseResponse<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("[MethodArgumentTypeMismatchException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "请求参数类型错误");
    }

    /**
     * 参数校验异常 MethodArgumentNotValidException
     * 415
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR);
    }

    /**
     * 参数校验异常 BindException
     * 415
     */
    @ExceptionHandler({BindException.class})
    public BaseResponse<?> bindExceptionHandler(BindException e) {
        log.error("[BindException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR);
    }

    /**
     * 参数校验异常 ConstraintViolationException
     * 415
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public BaseResponse<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR);
    }

    /**
     * 处理本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler({ValidationException.class})
    public BaseResponse<?> validationExceptionHandler(ValidationException e) {
        log.error("[ValidationException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR);
    }

    /**
     * 文件上传参数异常
     */
    @ExceptionHandler({MultipartException.class})
    public BaseResponse<?> multipartExceptionHandler(MultipartException e) {
        log.error("[MultipartException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "文件上传参数异常");
    }

    /**
     * 文件上传参数异常
     */
    @ExceptionHandler({MissingServletRequestPartException.class})
    public BaseResponse<?> missingServletRequestPartExceptionHandler(MissingServletRequestPartException e) {
        log.error("[MissingServletRequestPartException] ", e);
        return ResultUtils.error(ResponseCode.PARAMS_ERROR, "文件上传参数异常");
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     * 404
     * <p>
     * 需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public BaseResponse<?> noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        log.error("[NoHandlerFoundException] ", e);
        return ResultUtils.error(ResponseCode.NOT_FOUND_ERROR, "请求地址不存在");
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * 405
     * <p>
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public BaseResponse<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("[HttpRequestMethodNotSupportedException] ", e);
        return ResultUtils.error(ResponseCode.METHOD_NOT_ALLOW);
    }


    @ExceptionHandler({BusinessException.class})
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("[BusinessException] ", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException] ", e);
        return ResultUtils.error(ResponseCode.SYSTEM_ERROR);
    }
}
