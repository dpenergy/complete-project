package com.wudd.server.impl;

import com.wudd.mapper.ClazzMapper;
import com.wudd.pojo.Clazz;
import com.wudd.pojo.Result;
import com.wudd.server.ClazzServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClazzServerImpl implements ClazzServer {
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public Result queryClazzList() {
        List<Clazz> clazzList = clazzMapper.queryClazzList();
        if(clazzList.isEmpty()) {
            return Result.error("当前没有任何数据！");
        }

        return Result.success(clazzList);
    }
}
