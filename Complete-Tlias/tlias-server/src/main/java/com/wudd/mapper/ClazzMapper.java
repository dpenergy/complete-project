package com.wudd.mapper;

import com.wudd.pojo.Clazz;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClazzMapper {

    @Select("select c.*,e.name header_name from clazz c left join emp e on c.header_id=e.id")
    List<Clazz> queryClazzList();

    @Insert("insert into clazz(name,header_id) values (#{name},#{headerId})")
    void addClazz(Clazz clazz);


    @Delete("delete from clazz where id = #{id}")
    void deleteClazz(Integer id);

    @Select("select * from clazz where id = #{id}")
    Clazz queryClazzById(Integer id);

    // 确保健壮性我们采用动态SQL
    void updateClazz(Clazz clazz);
}
