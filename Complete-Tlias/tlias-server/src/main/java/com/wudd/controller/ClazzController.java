package com.wudd.controller;

import com.wudd.pojo.Clazz;
import com.wudd.pojo.Result;
import com.wudd.server.ClazzServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // 允许跨域请求
@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzServer clazzServer;

    @GetMapping
    public Result queryClazzList() {
        log.info("查询班级信息请求");
        return clazzServer.queryClazzList();
    }

}
