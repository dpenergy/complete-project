package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchInfo {
    private String name;
    private Integer gender;
    private String job;
    private LocalDate begin;
    private LocalDate end;
}
