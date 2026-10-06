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

    // 获取所有部门信息
    @GetMapping
    public Result getDeptList() {
        log.info("接收到部门列表查询");
        List<Dept> deptList = deptServer.getDeptList();
        log.info("获取到部门信息：{}",deptList);
        return Result.success(deptList);
    }

    //通过id查询部门，实现查询回显效果
    @GetMapping("/{id}")
    public Result queryDeptById(@PathVariable Integer id) {
        Dept dept = deptServer.queryDeptById(id);
        return Result.success(dept);
    }

    // 添加新部门
    @PostMapping
    public Result addDept(@RequestBody Dept dept) {
        log.info("接收到添加部门请求：{}",dept);
        Result result= deptServer.addDept(dept);
        log.info("响应添加部门请求：{}",result);
        return result;  // 只响应数据部处理任何逻辑
    }

    // 修改部门名称(前端只传递id和name属性)
    @PutMapping
    public Result updateDept(@RequestBody Dept dept) {
        return deptServer.updateDept(dept);
    }
}
