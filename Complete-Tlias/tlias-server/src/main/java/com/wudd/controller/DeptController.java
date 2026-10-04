package com.wudd.controller;

import com.wudd.pojo.Dept;
import com.wudd.pojo.Result;
import com.wudd.server.DeptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin // 允许跨域请求访问到这里
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptServer deptServer;

    @GetMapping
    public Result getDeptList() {
        log.info("接收到部门列表查询");
        List<Dept> deptList = deptServer.getDeptList();
        log.info("获取到部门信息：{}",deptList);
        return Result.success(deptList);
    }
}
