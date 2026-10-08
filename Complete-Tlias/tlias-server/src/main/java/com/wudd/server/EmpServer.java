package com.wudd.server;

import com.wudd.pojo.Result;
import com.wudd.pojo.SearchInfo;

public interface EmpServer {
    Result queryEmpList(SearchInfo searchInfo);
}
