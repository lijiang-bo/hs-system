package com.jsu.controller;

import com.jsu.anno.LogOperation;
import com.jsu.pojo.Clazz;
import com.jsu.pojo.ClazzQueryParam;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询班级
     *
     * @param clazzQueryParam 查询条件
     * @return 分页查询结果
     */
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("查询请求参数: {}", clazzQueryParam);
        PageResult pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增班级信息
     */
    @PostMapping
    @LogOperation
    public Result save(@RequestBody Clazz clazz) {
        log.info("请求参数clazz: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 根据ID查询班级
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }


    /**
     * 修改班级信息
     */
    @PutMapping
    @LogOperation
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级信息, clazz: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 删除班级信息
     */
    @DeleteMapping("/{id}")
    @LogOperation
    public Result delete(@PathVariable Long id) {
        log.info("删除班级信息, id: {}", id);
        clazzService.delete(id);
        return Result.success();
    }

    /**
     * 查询所有班级
     */
    @GetMapping("/list")
    public Result findAll() {
        List<Clazz> clazzList = clazzService.findAll();
        return Result.success(clazzList);
    }
}
