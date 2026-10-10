package com.wudd.server.impl;

import com.wudd.mapper.EmpMapper;
import com.wudd.pojo.*;
import com.wudd.server.EmpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServerImpl implements EmpServer {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public Result queryEmpList(SearchInfo searchInfo) {
        int total = empMapper.queryEmpCount(searchInfo);
        if(total == 0) return Result.error("没有查询到相关数据");

        List<Emp> rows = empMapper.queryEmpList(searchInfo);

        return Result.success(new SearchResult(total, rows));
    }

    @Transactional // 同时修改：emp,exprList两张表
    @Override
    public Result addEmp(Emp emp) {//name,username,gender,avatar,deptId,jobId,exprList
        // 1. 查询用户名判断用户名是否已经存在
        if(null != empMapper.queryEmpByUsernam(emp.getUsername())) {
            return Result.error("用户名："+emp.getUsername()+"已经存在");
        }

        // 2. 添加员工
        emp.setEntryDate(LocalDate.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.addEmp(emp); // 同时通过主键返回获取到员工的主键id

        // 3. 添加经历
        if(emp.getExprList().isEmpty()) return Result.successWithMsg("✔添加成功！");

        for (Expr expr : emp.getExprList()) {
            expr.setEmpId(emp.getId());
        }
        empMapper.addExpr(emp.getExprList());

        return Result.successWithMsg("✔添加成功！");
    }

    //@Transactional
    //@Override
    //public Result deleteEmp(Integer id) {
    //    // 1. 首先查询是否有这个id存在
    //    if(null == empMapper.queryEmpById(id)) {
    //        return Result.error("数据内不存在该员工的信息！");
    //    }
    //
    //    // 2. 删除员工经历
    //    empMapper.deleteExpr(id);
    //
    //    // 3. 删除员工
    //    empMapper.deleteEmp(id);
    //
    //    return Result.successWithMsg("已删除该员工的相关信息！");
    //}

    @Transactional
    @Override
    public Result deleteEmps(List<Integer> ids) {
        if(ids.isEmpty()) return Result.error("未选中任何数据!");

        // 前端可以看到说明数据存在，所以不用校验id值，直接操作就行了
        empMapper.deleteEmps(ids);
        empMapper.deleteExprs(ids);

        return Result.successWithMsg("已全部删除!");
    }
}
