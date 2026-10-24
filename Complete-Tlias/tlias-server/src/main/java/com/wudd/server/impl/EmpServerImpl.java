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
    public Result queryEmpList(EmpSearchInfo empSearchInfo) {
        int total = empMapper.queryEmpCount(empSearchInfo);
        if(total == 0) return Result.error("没有查询到相关数据");

        // limit部分不接收算术式,同时如果传递两个参数给mapper接口在用xml映射就得自己配置参数映射，所以自己封装到searchInfo里面
        empSearchInfo.setStartIndex((empSearchInfo.getCurrentPage() - 1) *  empSearchInfo.getPageSize());
        System.out.println(empSearchInfo.getStartIndex());
        List<Emp> rows = empMapper.queryEmpList(empSearchInfo);

        return Result.success(new EmpSearchResult(total, rows));
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

    @Transactional
    @Override
    public Result deleteEmps(List<Integer> ids) {
        if(ids.isEmpty()) return Result.error("未选中任何数据!");

        // 前端可以看到说明数据存在，所以不用校验id值，直接操作就行了
        empMapper.deleteEmps(ids);
        empMapper.deleteExprs(ids);

        return Result.successWithMsg("已全部删除!");
    }

    @Transactional
    @Override
    public Result updateEmp(Emp emp) { // emp是完整的员工信息
        emp.setUpdateTime(LocalDateTime.now());

        // 由于工作经历的empId属性只有在后端处理数据的时候才会用到，所以在前端没有去设置
        emp.getExprList().forEach(expr -> expr.setEmpId(emp.getId()));

        // 1. 该员工原有的所有工作经历，然后在添加新的

        empMapper.deleteExprByEmpId(emp.getId()); // 如果没有查询到就没有查询到
        if(!emp.getExprList().isEmpty()) {
            empMapper.addExpr(emp.getExprList());
        }

        // 2. 重置所有的员工字段
        // 用户名不可改变
        Emp oldEmp = empMapper.queryEmpById(emp.getId());
        if(!oldEmp.getUsername().equals(emp.getUsername())) {
            return Result.error("用户名不修改!");
        }

        empMapper.updateEmp(emp);

        return Result.successWithMsg("已更新员工信息!");
    }

    @Override
    public Result queryHeaderList() {
        List<Emp> headerList = empMapper.queryHeaderList();
        if(headerList.isEmpty()) {
            return Result.error("当前没有任何可选的班主任信息！");
        }

        return Result.success(headerList);
    }
}
