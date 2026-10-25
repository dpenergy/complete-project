package com.wudd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobData {
    private String name;
    private Integer boyCounts;
    private Integer girlCounts;
}
