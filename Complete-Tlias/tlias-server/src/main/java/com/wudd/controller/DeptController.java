package com.wudd.controller;

import com.wudd.pojo.Dept;
import com.wudd.pojo.Result;
import com.wudd.server.DeptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        log.info("部门列表查询请求"); // 具体的日志信息在SpringAOP中实现
        List<Dept> deptList = deptServer.getDeptList();
        return Result.success(deptList);
    }

    //通过id查询部门，实现查询回显效果
    @GetMapping("/{id}")
    public Result queryDeptById(@PathVariable Integer id) {
        log.info("id查询部门请求");
        Dept dept = deptServer.queryDeptById(id);
        return Result.success(dept);
    }

    @PostMapping
    public Result addDept(@RequestBody Dept dept) { // 只传递name属性
        log.info("添加部门请求"); // 具体执行信息在SpringAOP中实现
        return deptServer.addDept(dept);
    }

    @PutMapping
    public Result updateDept(@RequestBody Dept dept) {// 只传递id和name属性
        log.info("修改部门请求");
        deptServer.updateDept(dept);
        return Result.successWithMsg("Modification successful!");
    }

    @DeleteMapping("/{id}")
    public Result deleteDept(@PathVariable Integer id) {
        log.info("删除部门请求");
        deptServer.delteDeptById(id);
        return Result.successWithMsg("Delete successful!");
    }
}
