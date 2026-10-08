package com.wudd.mapper;

import com.wudd.pojo.Emp;
import com.wudd.pojo.SearchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpMapper {
    int queryEmpCount(SearchInfo searchInfo);

    List<Emp> queryEmpList(SearchInfo searchInfo);
}
