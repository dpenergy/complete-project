package com.wudd.controller;

import com.wudd.pojo.Emp;
import com.wudd.pojo.Result;
import com.wudd.pojo.SearchInfo;
import com.wudd.server.EmpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Result addEmp(@RequestBody  Emp emp){//name,username,gender,avatar,deptId,jobId,exprList
        log.info("新增员工请求");
        System.out.println(emp);
        

        return Result.successWithMsg("成功添加");
    }
}
