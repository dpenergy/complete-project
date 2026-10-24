package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private Integer gender;
    private Integer clazzId;
    private String clazzName;
    private String phone;
    private String homePhone;
    private Integer punishCounts;
    private Integer performance;
    private Integer status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDate;
}
