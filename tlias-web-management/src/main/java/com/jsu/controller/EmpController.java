package com.jsu.controller;

import com.jsu.anno.LogOperation;
import com.jsu.pojo.Emp;
import com.jsu.pojo.EmpQueryParam;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理
 */
@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 分页查询员工
     *
     * @param empQueryParam 查询条件
     * @return 分页查询结果
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("查询请求参数: {}", empQueryParam);
        PageResult pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 查询全部员工
     *
     * @return 员工列表
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询全部员工");
        List<Emp> empList = empService.findAll();
        return Result.success(empList);
    }

    /**
     * 添加员工
     */
    @PostMapping
    @LogOperation
    public Result save(@RequestBody Emp emp) {
        log.info("请求参数emp: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 批量删除员工
     */
    @DeleteMapping
    @LogOperation
    public Result delete(@RequestParam("ids") List<Integer> ids) {
        log.info("批量删除部门: ids: {}", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 查询回显
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据id查询员工的详细信息");
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 更新员工信息
     */
    @PutMapping
    @LogOperation
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工信息, {}", emp);
        empService.update(emp);
        return Result.success();
    }
}
