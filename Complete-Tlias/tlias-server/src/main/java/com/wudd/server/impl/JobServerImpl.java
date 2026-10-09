package com.wudd.server.impl;

import com.wudd.mapper.JobMapper;
import com.wudd.pojo.Job;
import com.wudd.pojo.Result;
import com.wudd.server.JobServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobServerImpl implements JobServer {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public Result queryJobList() {
        List<Job> jobList = jobMapper.queryJobList();
        if(jobList.isEmpty()){
            return Result.error("职位列表查询失败");
        }

        return Result.success(jobList);
    }
}
