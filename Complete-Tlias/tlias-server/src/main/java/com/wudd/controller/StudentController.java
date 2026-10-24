package com.wudd.controller;

import com.wudd.pojo.Result;
import com.wudd.pojo.Student;
import com.wudd.pojo.StudentSearchInfo;
import com.wudd.server.StudentServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentServer studentServer;

    @GetMapping("/{id}")
    public Result queryClazzStudents(@PathVariable Integer id) {
        log.info("查询班级内学生请求");
        return studentServer.queryClazzStudents(id);
    }

    @GetMapping
    public Result queryStudentList(StudentSearchInfo studentSearchInfo) {
        log.info("学生列表查询请求");
        System.out.println(studentSearchInfo);
        return studentServer.queryStudentList(studentSearchInfo);
    }

    @PostMapping
    public Result addStudent(@RequestBody Student student) {
        log.info("新增学生请求");
        return studentServer.addStudent(student);
    }

    //@DeleteMapping("/{id}")
    //public Result deleteStudent(@PathVariable Integer id) {
    //    log.info("删除学员请求");
    //    return studentServer.deleteStudent(id);
    //}

    @DeleteMapping
    public Result batchDeleteStudent(@RequestParam List<Integer> ids) {
        log.info("批量删除学员请求");
        return studentServer.batchDeleteStudent(ids);
    }

    @PutMapping
    public Result updateStudent(@RequestBody Student student) {
        log.info("修改学员信息");
        return studentServer.updateStudent(student);
    }
}
