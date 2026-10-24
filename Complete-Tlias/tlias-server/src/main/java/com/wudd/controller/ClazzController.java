package com.wudd.controller;

import com.wudd.pojo.Clazz;
import com.wudd.pojo.Result;
import com.wudd.server.ClazzServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Result addClazz(@RequestBody Clazz clazz) {
        log.info("添加班级请求");
        return clazzServer.addClazz(clazz);
    }

    @DeleteMapping
    public Result deleteClazz(Integer id) {
        log.info("删除班级请求");
        return clazzServer.deleteClazz(id);
    }

    @PutMapping
    public Result updateClazz(@RequestBody Clazz clazz) {
        log.info("编辑班级信息提交请求");
        return clazzServer.updateClazz(clazz);
    }
}
