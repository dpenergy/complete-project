package com.wudd.exception;

import com.wudd.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 异常会自动往上抛，即便没有手动写throw它也会自动往上抛，最终都会抛到Controller层，所以全局处理异常是对Controller层的advice
    
    @ExceptionHandler(value = Exception.class) // 这里的value是字节码文件，可以省略，它能从方法参数中获取
    public Result handleException(Exception e) {
        log.error("全局异常处理器-拦截到异常：", e); // 异常参数做为最后一个参数能自动传入
        return Result.error("服务器出错了！请等待管理员处理！");
    }
}
