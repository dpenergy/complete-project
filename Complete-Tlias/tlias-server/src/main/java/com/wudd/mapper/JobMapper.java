package com.wudd.mapper;

import com.wudd.pojo.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface JobMapper {
    @Select("select * from job")
    List<Job> queryJobList();
}
