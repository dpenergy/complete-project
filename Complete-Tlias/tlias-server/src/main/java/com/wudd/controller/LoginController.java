package com.wudd.controller;

import com.wudd.pojo.Admi;
import com.wudd.pojo.Result;
import com.wudd.server.LoginServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private LoginServer admiServer;

    @PostMapping
    public Result login(@RequestBody Admi admi) { // username and password
        log.info("登录请求");
        return admiServer.login(admi);
    }
}
