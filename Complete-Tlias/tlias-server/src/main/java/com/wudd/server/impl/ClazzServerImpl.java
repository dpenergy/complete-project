package com.wudd.server.impl;

import com.wudd.mapper.ClazzMapper;
import com.wudd.pojo.Clazz;
import com.wudd.pojo.Result;
import com.wudd.server.ClazzServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public Result addClazz(Clazz clazz) { // name headerId
        clazzMapper.addClazz(clazz);
        return Result.success();
    }

    @Override
    public Result deleteClazz(Integer id) {
        // 健壮性考虑，先查再删
        // 如果前端能够显示该班级，那么一定是有该班级的，不用先查再删
        clazzMapper.deleteClazz(id);
        return Result.success();
    }

    @Override
    public Result updateClazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.updateClazz(clazz);
        return Result.success();
    }
}
