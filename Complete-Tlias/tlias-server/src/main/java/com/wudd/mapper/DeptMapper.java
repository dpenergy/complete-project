package com.wudd.mapper;

import com.wudd.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("select * from dept")
    List<Dept> getDeptList();

    @Select("select * from dept where name=#{name}")
    Dept queryDeptByName(String name);


    // 这里会直接将dept作为根对象来解析OGNL表达式，所以直接填属性就可以了name → dept.getName()
    // 不然会#{dept.name} → dept.getDept() → .getName() 但 Dept 类没有 getDept() 方法 ❌
    // 这里不是直接访问属性，底层是使用getter来调取属性
    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void addDept(Dept dept);

    @Select("select * from dept where id = #{id}")
    Dept queryDEptById(Integer id);

    @Update("update dept set name=#{name}, update_time=#{updateTime} where id=#{id}")
    void updateDept(Dept newDept);

    @Delete("delete from dept where id = #{id}")
    void deleteDeptById(Integer id);
}
