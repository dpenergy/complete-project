package com.wudd.mapper;

import com.wudd.pojo.Admi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdmiMapper { // 登录的是Admi账号
    @Select("select * from admi where username=#{username} and password=#{password}")
    Admi login(Admi admi);
}
