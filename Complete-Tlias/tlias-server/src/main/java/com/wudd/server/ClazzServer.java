package com.wudd.server;

import com.wudd.pojo.Clazz;
import com.wudd.pojo.Result;

public interface ClazzServer {
    Result queryClazzList();

    Result addClazz(Clazz clazz);

    Result deleteClazz(Integer id);

    Result updateClazz(Clazz clazz);
}
