package com.wudd.server;

import com.wudd.pojo.Dept;
import com.wudd.pojo.Result;

import java.util.List;

public interface DeptServer {
    List<Dept> getDeptList();

    Result addDept(Dept dept);
}
