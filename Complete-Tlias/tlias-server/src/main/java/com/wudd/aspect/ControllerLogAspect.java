package com.wudd.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect {
    // 实现Controller层的简单日志：记录接收到的请求以及请求参数，记录返回的Result

    @Pointcut("execution(* com.wudd.controller.*.*(..))")
    public void controllerAspectPath(){}

    /**
     * 该方法将Controller层的所有方法的名称，参数，接收到的参数，返回值，执行时间做了记录，controller层只需要简单的记录干什么就行了
     */
    @Around("controllerAspectPath()")
    public Object controllerLogAspect(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 获取方法执行时间
        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        // 2. 获取目标对象类名:示例 com.wudd.aspect.ControllerLogAspect
        String targetClassName = pjp.getTarget().getClass().getName();

        // 3. 获取方法标签和参数:示例 method(name=wudd,age=100001)
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] paramValues = pjp.getArgs();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paramNames.length; i++) {
            if(i == 0) {
                sb.append(paramNames[i]).append("=").append(paramValues[i]);
                continue;
            }
            sb.append(", ").append(paramNames[i]).append("=").append(paramValues[i]);
        }
        String methodInfo = sb.toString().isEmpty() ? "无参数" : sb.toString();


        log.info("总结该次操作 请求调用："+targetClassName+"."+methodName+"("+methodInfo+") "+"执行耗时："+(endTime-startTime)+"ms  |"+"方法返回："+result);
        return result;
    }


}
