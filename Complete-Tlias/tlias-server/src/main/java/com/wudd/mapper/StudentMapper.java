package com.wudd.mapper;

import com.wudd.pojo.OptionResult;
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

    @Select("select\n" +
            "    sum(case when gender = 1 then 1 else 0 end) as one_counts,\n" +
            "    sum(case when gender = 2 then 1 else 0 end) as two_counts\n" +
            "from student")
    OptionResult genderCount();

    @Select("select\n" +
            "    sum(case when status = 1 then 1 else 0 end) as one_counts,\n" +
            "    sum(case when status = 2 then 1 else 0 end) as two_counts\n" +
            "from student;")
    OptionResult graduateCount();


    @Select("select\n" +
            "    sum(case when performance = 1 then 1 else 0 end) as one_counts,\n" +
            "    sum(case when performance = 2 then 1 else 0 end) as two_counts,\n" +
            "    sum(case when performance = 3 then 1 else 0 end) as three_counts,\n" +
            "    sum(case when performance = 4 then 1 else 0 end) as four_counts\n" +
            "from student")
    OptionResult performanceCount();
}
