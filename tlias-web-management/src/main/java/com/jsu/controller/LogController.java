package com.jsu.controller;

import com.jsu.pojo.LogQueryParam;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询日志信息
     *
     * @param logQueryParam 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public Result page(LogQueryParam logQueryParam) {
        log.info("分页查询日志信息, 参数: {}", logQueryParam);
        PageResult pageResult = logService.page(logQueryParam);
        return Result.success(pageResult);
    }
}
