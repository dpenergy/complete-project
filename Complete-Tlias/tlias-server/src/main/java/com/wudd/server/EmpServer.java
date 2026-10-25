package com.wudd.server;

import com.wudd.pojo.Emp;
import com.wudd.pojo.EmpSearchInfo;
import com.wudd.pojo.Result;

import java.util.List;

public interface EmpServer {
    Result queryEmpList(EmpSearchInfo empSearchInfo);

    Result addEmp(Emp emp);

    //Result deleteEmp(Integer id);

    Result deleteEmps(List<Integer> ids);

    Result updateEmp(Emp emp);

    Result queryHeaderList();

    Result genderCount();

    Result queryJobData();
}
