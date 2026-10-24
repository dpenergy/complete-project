package com.wudd.server;

import com.wudd.pojo.Result;
import com.wudd.pojo.Student;
import com.wudd.pojo.StudentSearchInfo;

import java.util.List;

public interface StudentServer {
    Result queryClazzStudents(Integer id);

    Result queryStudentList(StudentSearchInfo studentSearchInfo);

    Result addStudent(Student student);

    Result deleteStudent(Integer id);

    Result updateStudent(Student student);

    Result batchDeleteStudent(List<Integer> ids);
}
