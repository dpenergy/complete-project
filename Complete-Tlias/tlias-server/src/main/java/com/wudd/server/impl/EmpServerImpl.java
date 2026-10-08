package com.wudd.server.impl;

import com.wudd.mapper.EmpMapper;
import com.wudd.pojo.Emp;
import com.wudd.pojo.Result;
import com.wudd.pojo.SearchInfo;
import com.wudd.pojo.SearchResult;
import com.wudd.server.EmpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServerImpl implements EmpServer {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public Result queryEmpList(SearchInfo searchInfo) {
        int total = empMapper.queryEmpCount(searchInfo);
        if(total == 0) return Result.successWithMsg("没有查询到相关数据");

        List<Emp> rows = empMapper.queryEmpList(searchInfo);

        return Result.success(new SearchResult(total, rows));
    }
}
