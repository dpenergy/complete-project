package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchInfo {
    private String name;
    private String clazzName;
    private Integer gender;
    private Integer age;
    private Integer status;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 将前端传递的字符串日期转换为java日期对象
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    private Integer pageSize;
    private Integer currentPage;
    private Integer startIndex;
}
