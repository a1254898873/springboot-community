package com.nifengi.community.aspect;

import com.nifengi.community.util.JsonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Yu
 * @title: BindingResultAspect
 * @projectName community
 * @date 2022/8/1 20:20
 */
@Aspect
@Component
@Order(2)
public class BindingResultAspect {

    @Pointcut("execution(public * com.nifengi.community.controller.*.*(..))")
    public void BindingResult1() {
    }

    @Pointcut("execution(public * com.nifengi.community.controller.UserController.upload(..))")
    public void BindingResult2() {
    }

    @Pointcut("BindingResult1() && !BindingResult2()")
    public void BindingResult() {
    }

    @Around("BindingResult()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return JsonResult.fail(fieldError.getDefaultMessage());
                    } else {
                        return JsonResult.fail("请求参数错误！");
                    }
                }
            }
        }
        return joinPoint.proceed();
    }

}
