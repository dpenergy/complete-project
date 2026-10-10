package com.wudd.server;

import com.wudd.pojo.Emp;
import com.wudd.pojo.Result;
import com.wudd.pojo.SearchInfo;
import java.util.List;

public interface EmpServer {
    Result queryEmpList(SearchInfo searchInfo);

    Result addEmp(Emp emp);

    //Result deleteEmp(Integer id);

    Result deleteEmps(List<Integer> ids);
}
