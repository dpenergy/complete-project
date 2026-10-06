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

    @Override
    public Dept queryDeptById(Integer id) {
        return deptMapper.queryDEptById(id);
    }

    @Override
    public Result updateDept(Dept dept) {// dept携带id和name属性
        // 根据id查询到原始dept
        Dept newDept = deptMapper.queryDEptById(dept.getId());

        // dept中只有name和id属性，补充updateTime
        newDept.setUpdateTime(LocalDateTime.now());

        // 更新name属性
        newDept.setName(dept.getName());

        // 提交newDept
        deptMapper.updateDept(newDept);

        return Result.success();
    }
}
