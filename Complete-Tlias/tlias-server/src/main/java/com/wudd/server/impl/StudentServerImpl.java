package com.wudd.server.impl;

import com.wudd.mapper.StudentMapper;
import com.wudd.pojo.Result;
import com.wudd.pojo.Student;
import com.wudd.pojo.StudentSearchInfo;
import com.wudd.pojo.StudentSearchResult;
import com.wudd.server.StudentServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServerImpl implements StudentServer {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result queryClazzStudents(Integer id) {
        List<Student> studentList = studentMapper.queryClazzStudents(id);
        if(studentList.isEmpty()) return Result.error("当前该班级未添加过学生！");

        return Result.success(studentList);
    }


    @Override
    public Result queryStudentList(StudentSearchInfo studentSearchInfo) {
        // 手动实现分页查询
        studentSearchInfo.setStartIndex((studentSearchInfo.getCurrentPage()-1) *  studentSearchInfo.getPageSize());

        int total = studentMapper.queryTotal(studentSearchInfo);

        List<Student> studentList = studentMapper.queryStudentList(studentSearchInfo);
        if(studentList.isEmpty()) {
            return Result.error("未查询到任何数据！");
        }

        return Result.success(new StudentSearchResult(total, studentList));
    }

    @Override
    public Result addStudent(Student student) {
        // 必携带：name,gender,clazzId属性；可能携带age,phone,homePhone属性
        studentMapper.addStudent(student);
        return Result.success();
    }

    @Override
    public Result deleteStudent(Integer id) {
        // 关联的所有数据都应该一起删除
        // 学生信息，关联的考试成绩信息
        studentMapper.deleteStudent(id);

        return Result.successWithMsg("成功移除该同学");
    }

    @Override
    public Result updateStudent(Student student) {
        studentMapper.updateStudent(student);
        return Result.successWithMsg("成功修改"+student.getName()+"的信息！");
    }

    @Override
    public Result batchDeleteStudent(List<Integer> ids) {
        // 确保ids是有内容的，不然数据就全被删除了
        if(ids.isEmpty()) return Result.error("未包含删除信息！");

        studentMapper.batchDeleteStudent(ids);
        return Result.successWithMsg("删除成功！");
    }
}
