package com.wudd.controller;

import com.wudd.pojo.Result;
import com.wudd.pojo.SearchInfo;
import com.wudd.pojo.SearchResult;
import com.wudd.server.EmpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // 允许跨域访问
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpServer empServer;

    @GetMapping
    public Result queryEmpList(SearchInfo searchInfo){ // 前端是简单参数，可以之间使用对象接受
        // name,gender,job,begin,end
        log.info("查询员工列表请求");
        return empServer.queryEmpList(searchInfo);
    }
}
