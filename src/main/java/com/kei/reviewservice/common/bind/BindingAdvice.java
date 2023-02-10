package com.kei.reviewservice.common.bind;

import com.kei.reviewservice.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Slf4j
//@Component
//@Aspect
public class BindingAdvice {

//    @Around("execution(* com.kei.reviewservice.business.*.controller.*Controller.*(..))")
//    public Object validationCheck(ProceedingJoinPoint joinPoint) throws Throwable {
//        String [] type = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
//        String method = joinPoint.getSignature().getName();
//
//        Object [] parameters = Arrays.stream(joinPoint.getArgs())
//                .map(arg -> !arg.toString().contains("error") ? arg : "").toArray();
//
//        log.info("[{}][{}] Args: [{}]", type[type.length - 1], method, Arrays.toString(parameters));
//
//        for (Object arg : joinPoint.getArgs()) {
//            if (arg instanceof BindingResult && ((BindingResult) arg).hasErrors()) {
//                Map<String, Object> errorMap = new HashMap<>();
//                final List<FieldError> fieldErrors = ((BindingResult) arg).getFieldErrors();
//
//                for (FieldError fieldError : fieldErrors) {
//                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//
//                    log.warn("[{}][{}] Field [{}] Message [{}]", type[type.length - 1], method, fieldError.getField(), fieldError.getDefaultMessage());
//                }
//                log.info("errorMap: {}", errorMap.toString());
////                throw new ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), (BindingResult) arg);
//            }
//        }
//
//        return joinPoint.proceed();
//    }
}
