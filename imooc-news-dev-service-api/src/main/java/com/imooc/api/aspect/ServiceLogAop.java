package com.imooc.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAop {

    static final Logger logger = LoggerFactory.getLogger(ServiceLogAop.class);

    /**
     * AOP通知：
     * 前置通知：调用service方法之前执行
     * 后置通知：
     * 环绕通知：调用service方法之前之后执行
     * 异常通知：
     * 最终通知：service执行完毕之后，通知
     */
    @Around("execution(* com.imooc.*.service.impl..*.*(..))")
    public Object recordTimeOfService(ProceedingJoinPoint point) throws Throwable {
        logger.info("====开始执行{}.{}====",
                point.getTarget().getClass(),
                point.getSignature().getName());
        long start = System.currentTimeMillis();

        long end = System.currentTimeMillis();
        Object proceed = point.proceed();
        long taskTime = end - start;

        if (taskTime > 3000) {
            logger.error("当前执行耗时 {}", taskTime);
        } else if (taskTime > 2000) {
            logger.warn("当前执行耗时 {}", taskTime);
        } else {
            logger.info("当前执行耗时 {}", taskTime);
        }
        return proceed;
    }



}
