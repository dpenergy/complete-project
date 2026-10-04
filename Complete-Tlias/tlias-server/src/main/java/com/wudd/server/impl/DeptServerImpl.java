package com.wudd.server.impl;

import com.wudd.mapper.DeptMapper;
import com.wudd.pojo.Dept;
import com.wudd.server.DeptServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServerImpl implements DeptServer {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> getDeptList() {
        return deptMapper.getDeptList();
    }
}
