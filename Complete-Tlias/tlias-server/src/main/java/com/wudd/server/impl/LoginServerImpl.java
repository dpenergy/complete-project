package com.wudd.server.impl;

import com.wudd.mapper.AdmiMapper;
import com.wudd.pojo.Admi;
import com.wudd.pojo.Result;
import com.wudd.server.LoginServer;
import com.wudd.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServerImpl implements LoginServer {
    @Autowired
    private AdmiMapper admiMapper;

    @Override
    public Result login(Admi admi) {
        // 查询是否存在
        if(null != admiMapper.login(admi)) {
            Admi fullAdmi = admiMapper.login(admi);
            // 下发令牌
            String token = JwtUtils.generateToken(fullAdmi.getUsername());
            fullAdmi.setToken(token);

            return Result.success(fullAdmi);
        }

        return Result.error("用户名或密码错误！");
    }
}
