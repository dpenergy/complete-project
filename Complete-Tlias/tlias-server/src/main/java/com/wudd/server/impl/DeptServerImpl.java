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
            return Result.successWithMsg(dept.getName()+" add successfully!");
        }

        return Result.error(dept.getName()+" already exist! Please do not repeatedly add it!");
    }

    @Override
    public Dept queryDeptById(Integer id) {
        return deptMapper.queryDEptById(id);
    }

    @Override
    public void updateDept(Dept newDept) {// dept携带id和name属性
        // 根据id查询到原始dept
        Dept originalDept = deptMapper.queryDEptById(newDept.getId());

        // 补充updateTime,和createTime
        newDept.setUpdateTime(LocalDateTime.now());
        originalDept.setCreateTime(originalDept.getCreateTime());

        // 提交newDept
        deptMapper.updateDept(newDept);
    }

    @Override
    public void delteDeptById(Integer id) {
        deptMapper.deleteDeptById(id);
    }
}
