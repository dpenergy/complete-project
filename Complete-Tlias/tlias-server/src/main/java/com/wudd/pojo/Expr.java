package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expr {
    private Integer id;
    private String company;
    private LocalDate begin;
    private LocalDate end;
    private String job;
    private Integer empId; // 前端用不到，只有到服务器添加工作经历的时候才有用
}
