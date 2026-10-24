package com.wudd.mapper;

import com.wudd.pojo.Student;
import com.wudd.pojo.StudentSearchInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("select * from student where clazz_id = #{id}")
    List<Student> queryClazzStudents(Integer id); // 这里的id是班级的id

    List<Student> queryStudentList(StudentSearchInfo studentSearchInfo);


    int queryTotal(StudentSearchInfo studentSearchInfo);

    void addStudent(Student student);

    @Delete("delete from student where id = #{id}")
    void deleteStudent(Integer id);

    void updateStudent(Student student);

    void batchDeleteStudent(List<Integer> ids);
}
