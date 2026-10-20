package com.jsu.controller;


import com.jsu.anno.LogOperation;
import com.jsu.anno.RequireRole;
import com.jsu.pojo.Dept;
import com.jsu.pojo.Result;
import com.jsu.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 */
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 查询全部部门数据
     */
    @GetMapping
    public Result list() {
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * 根据ID删除部门
     */
    @DeleteMapping("/{id}")
    @LogOperation
    @RequireRole(exclude = {3})
    public Result delete(@PathVariable Integer id) {
        log.info("根据id删除部门, id: {}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增部门
     */
    @PostMapping
    @LogOperation
    @RequireRole(exclude = {3})
    public Result add(@RequestBody Dept dept) {
        log.info("新增部门, dept: {}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 根据ID查询部门
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询, id: {}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     */
    @PutMapping
    @LogOperation
    @RequireRole(exclude = {3})
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门, dept: {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
