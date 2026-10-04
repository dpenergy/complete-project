package com.wudd.server.impl;

import com.wudd.mapper.DeptMapper;
import com.wudd.pojo.Dept;
import com.wudd.pojo.Result;
import com.wudd.server.DeptServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServerImpl implements DeptServer {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> getDeptList() {
        return deptMapper.getDeptList();
    }

    @Override
    public Result addDept(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        // 先查找是否已经存在
        if(deptMapper.queryDeptByName(dept.getName()) == null) {
            deptMapper.addDept(dept);
            return Result.success();
        }

        return Result.error("Duplicate dept name!");
    }
}
