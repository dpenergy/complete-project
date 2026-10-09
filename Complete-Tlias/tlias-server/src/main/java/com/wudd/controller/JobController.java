package com.wudd.controller;

import com.wudd.pojo.Result;
import com.wudd.server.JobServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RequestMapping("/jobs")
@RestController
public class JobController {
    @Autowired
    private JobServer jobServer;

    @GetMapping
    public Result queryJobList() {
        log.info("职位列表查询");
        return jobServer.queryJobList();
    }
}
