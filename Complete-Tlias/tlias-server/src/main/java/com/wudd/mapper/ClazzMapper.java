package com.wudd.mapper;

import com.wudd.pojo.Clazz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClazzMapper {

    @Select("select c.*,e.name header_name from clazz c left join emp e on c.header_id=e.id")
    List<Clazz> queryClazzList();
}
