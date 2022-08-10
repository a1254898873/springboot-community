package com.nifengi.community.exception;

import com.nifengi.community.entity.response.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yu
 * @title: GlobalExceptionHandler
 * @projectName community
 * @date 2022/7/27 12:02
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public JsonResult bizExceptionHandler(BaseException e) {
        return JsonResult.defineError(e);
    }

    /**
     * 处理其他异常
     *
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult exceptionHandler( Exception e) {
        System.out.println(e);
        return JsonResult.otherError(ErrorEnum.INTERNAL_SERVER_ERROR);
    }
}
