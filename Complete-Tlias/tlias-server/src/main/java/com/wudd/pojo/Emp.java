package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    private Integer id;
    private String name;
    private String username;
    private Integer gender;
    private String avatar;
    private String dept;
    private String job;
    private LocalDate entryDate;
    private LocalDateTime updateTime;
    private List<Expr> exprList;
}
