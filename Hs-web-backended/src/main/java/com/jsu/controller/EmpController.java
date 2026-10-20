package com.jsu.controller;

import com.jsu.anno.LogOperation;
import com.jsu.anno.RequireRole;
import com.jsu.pojo.Emp;
import com.jsu.pojo.EmpQueryParam;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.service.EmpService;
import com.jsu.util.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
     * 分页查询员工，根据角色权限过滤
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("查询请求参数: {}", empQueryParam);
        Integer currentRole = CurrentHolder.getCurrentRole();
        // role = 2 管理员和 role = 3 普通员工都只能查询 role = 3 的员工
        if (currentRole != null && (currentRole == 2 || currentRole == 3)) {
            empQueryParam.setRole(3);
        }
        PageResult pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 查询全部员工，根据角色权限过滤
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询全部员工");
        List<Emp> empList = empService.findAll();
        Integer currentRole = CurrentHolder.getCurrentRole();
        // role = 2 管理员和 role = 3 普通员工都只返回 role = 3 的员工
        if (currentRole != null && (currentRole == 2 || currentRole == 3)) {
            empList = empList.stream()
                    .filter(e -> e.getRole() != null && e.getRole() == 3)
                    .collect(Collectors.toList());
        }
        return Result.success(empList);
    }

    /**
     * 添加员工，根据角色权限校验
     */
    @PostMapping
    @LogOperation
    @RequireRole(exclude = {3})
    public Result save(@RequestBody Emp emp) {
        log.info("请求参数emp: {}", emp);
        Integer currentRole = CurrentHolder.getCurrentRole();
        // role = 2管理员只能添加role = 3普通员工
        if (currentRole != null && currentRole == 2) {
            if (emp.getRole() == null || emp.getRole() != 3) {
                return Result.error("无权限添加非普通员工");
            }
        }
        empService.save(emp);
        return Result.success();
    }

    /**
     * 批量删除员工，根据角色权限校验
     */
    @DeleteMapping
    @LogOperation
    @RequireRole(exclude = {3})
    public Result delete(@RequestParam("ids") List<Integer> ids) {
        log.info("批量删除员工: ids: {}", ids);
        Integer currentRole = CurrentHolder.getCurrentRole();
        Integer currentId = CurrentHolder.getCurrentId();

        // 禁止操作人删除自己
        if (currentId != null && ids.contains(currentId)) {
            return Result.error("不能删除当前登录用户自身");
        }

        // role = 2管理员只能删除role = 3的员工
        if (currentRole != null && currentRole == 2) {
            for (Integer id : ids) {
                Emp dbEmp = empService.getInfo(id);
                if (dbEmp == null) {
                    return Result.error("员工不存在，id=" + id);
                }
                if (dbEmp.getRole() == null || dbEmp.getRole() != 3) {
                    return Result.error("无权限删除非普通员工");
                }
            }
        }

        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 查询回显，根据角色权限校验
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据id查询员工的详细信息");
        Emp emp = empService.getInfo(id);
        if (emp == null) {
            return Result.error("员工不存在");
        }
        Integer currentRole = CurrentHolder.getCurrentRole();
        // role = 2 管理员和 role = 3 普通员工只能查看 role = 3 的员工详情
        if (currentRole != null && (currentRole == 2 || currentRole == 3)) {
            if (emp.getRole() == null || emp.getRole() != 3) {
                return Result.error("无权限查看该员工信息");
            }
        }
        return Result.success(emp);
    }

    /**
     * 更新员工信息，根据角色权限校验
     */
    @PutMapping
    @LogOperation
    @RequireRole(exclude = {3})
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工信息, {}", emp);

        // 管理员(role = 2)只能修改普通员工(role = 3)，且不允许变更角色
        // 例外：管理员可以编辑自己的信息（修改个人资料）
        Integer currentRole = CurrentHolder.getCurrentRole();
        log.info("当前用户角色: role={}", currentRole);

        Integer currentId = CurrentHolder.getCurrentId();
        boolean isSelfEdit = currentId != null && emp.getId() != null && currentId.equals(emp.getId());

        if (currentRole != null && currentRole == 2 && emp.getId() != null && !isSelfEdit) {
            Emp dbEmp = empService.getInfo(emp.getId());
            log.info("目标员工: id={}, role={}", emp.getId(), dbEmp != null ? dbEmp.getRole() : null);
            if (dbEmp == null) {
                return Result.error("员工不存在");
            }
            if (dbEmp.getRole() == null || dbEmp.getRole() != 3) {
                return Result.error("无权限修改非普通员工信息");
            }
            // 如果管理员试图修改角色字段，拒绝并返回错误
            if (emp.getRole() != null && emp.getRole() != 3) {
                log.info("管理员试图将员工角色从3改为{}，已拒绝", emp.getRole());
                return Result.error("无权限将员工角色提升为管理员或超级管理员");
            }
            // 强制将角色设置为普通员工
            emp.setRole(3);
        }

        empService.update(emp);
        return Result.success();
    }
}
