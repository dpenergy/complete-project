package com.wudd.mapper;

import com.wudd.pojo.Emp;
import com.wudd.pojo.Expr;
import com.wudd.pojo.SearchInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface EmpMapper {// 同时处理emp表和expr表
    // XML映射实现SQL语句
    int queryEmpCount(SearchInfo searchInfo);

    List<Emp> queryEmpList(SearchInfo searchInfo);

    void addExpr(List<Expr> exprList);

    void deleteEmps(List<Integer> ids);

    void deleteExprs(List<Integer> ids);

    // 注解实现SQL语句
    @Select("select * from emp where username = #{username}")
    Object queryEmpByUsernam(String username);

    @Insert("insert into emp(name, username, gender, avatar, dept_id, job_id) values(#{name},#{username},#{gender},#{avatar},#{deptId},#{jobId})")
    @Options(useGeneratedKeys = true,keyProperty = "id") // 将返回的主键赋值给对象emp的id属性
    void addEmp(Emp emp);

    @Select("select * from emp where id = #{id}")
    Emp queryEmpById(Integer id);

    @Delete("delete from expr where emp_id = #{id}")
    void deleteExprByEmpId(Integer id);

    @Update("update emp set name=#{name}, gender=#{gender}, avatar=#{avatar}, dept_id=#{deptId},job_id=#{jobId}, update_time=#{updateTime} where id=#{id}")
    void updateEmp(Emp emp);

    @Select("select * from emp where emp.job_id = 2 or emp.job_id = 9")
    List<Emp> queryHeaderList();

    //@Delete("delete from expr where emp_id = #{id}")
    //void deleteExpr(Integer id);

    //@Delete("delete from emp where id = #{id}")
    //void deleteEmp(Integer id);


}
