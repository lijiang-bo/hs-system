package com.jsu.controller;

import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.pojo.StudentDisciplineQueryParam;
import com.jsu.service.StudentDisciplineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/disciplines")
@RestController
public class StudentDisciplineController {

    @Autowired
    private StudentDisciplineService studentDisciplineService;

    /**
     * 分页查询学员违纪记录
     */
    @GetMapping
    public Result page(StudentDisciplineQueryParam studentDisciplineQueryParam) {
        log.info("分页查询学员违纪记录,参数:{}", studentDisciplineQueryParam);
        PageResult pageResult = studentDisciplineService.page(studentDisciplineQueryParam);
        return Result.success(pageResult);
    }
}
