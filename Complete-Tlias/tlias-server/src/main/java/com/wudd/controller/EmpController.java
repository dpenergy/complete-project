package com.wudd.controller;

import com.wudd.mapper.EmpMapper;
import com.wudd.pojo.Emp;
import com.wudd.pojo.Result;
import com.wudd.pojo.EmpSearchInfo;
import com.wudd.server.EmpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin // 允许跨域访问
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpServer empServer;

    @GetMapping
    public Result queryEmpList(EmpSearchInfo empSearchInfo){ // 前端是简单参数，可以之间使用对象接受
        // name,gender,job,begin,end
        log.info("查询员工列表请求");
        return empServer.queryEmpList(empSearchInfo);
    }

    @PostMapping
    public Result addEmp(@RequestBody Emp emp){//name,username,gender,avatar,deptId,jobId,exprList
        log.info("新增员工请求");
        //System.out.println(emp);
        return empServer.addEmp(emp);
    }

    //@DeleteMapping // 简单参数可以直接接收
    //public Result deleteEmp(Integer id){
    //    log.info("删除员工请求");
    //    return empServer.deleteEmp(id);
    //}

    // 将批量删除和单条数据删除合并为一个请求，单条数据则是特殊情况
    @DeleteMapping
    public Result deleteEmps(@RequestParam List<Integer> ids){ // 使用数组接收前端数据无法获取有效日志，采用集合
        log.info("批量删除员工请求");
        return empServer.deleteEmps(ids);
    }

    @PutMapping
    public Result updateEmp(@RequestBody Emp emp){
        log.info("修改员工信息请求");
        return empServer.updateEmp(emp);
    }

    @GetMapping("/headers")
    public Result queryHeaderList() {
        log.info("可选班主任信息查询");
        return empServer.queryHeaderList();
    }

    @GetMapping("/gender")
    public Result genderCount() {
        log.info("员工性别统计请求");
        return empServer.genderCount();
    }

    @GetMapping("/jobs")
    public Result queryJobData() {
        log.info("查询职位信息请求");
        return empServer.queryJobData();
    }
}
