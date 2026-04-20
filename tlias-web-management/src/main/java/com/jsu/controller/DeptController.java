package com.jsu.controller;


import com.jsu.anno.LogOperation;
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
    //@RequestMapping(value = "/depts", method = RequestMethod.GET)
    //@GetMapping("/depts")
    @GetMapping
    public Result list() {
        //System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }


    /**
     根据ID删除部门 - 简单参数接收: 方式一 (HttpServletRequest)
     */
//    @DeleteMapping("/depts")
//    public Result delete(HttpServletRequest request) {
//        String idStr = request.getParameter("id");
//        int id = Integer.parseInt(idStr);
//
//        System.out.println("根据ID删除部门: " + id);
//        return Result.success();
//    }

    /**
     * 根据ID删除部门 - 简单参数接收: 方式二 (@RequestParam)
     */
//    @DeleteMapping("/depts")
//    public Result delete(@RequestParam("id") Integer deptId){
//        System.out.println("根据ID删除部门: " + deptId);
//        return Result.success();
//    }

    /**
     * 根据ID删除部门 - 简单参数接收: 方式三 (@PathVariable)
     */
    //方案三：如果请求参数名与形参变量名相同，直接定义方法形参即可接收。（省略@RequestParam）
    //@DeleteMapping("/depts")
    @DeleteMapping
    @LogOperation
    public Result delete(Integer id) {
        //System.out.println("根据ID删除部门: " + id);
        log.info("根据id删除部门, id: {}", id);
        deptService.deleteById(id);
        return Result.success();
    }


    /**
     * 新增部门 - POST http://localhost:8080/depts   请求参数：{"name":"研发部"}
     */
    //@PostMapping("/depts")
    @PostMapping
    @LogOperation
    public Result add(@RequestBody Dept dept) {
        //System.out.println("新增部门, dept=" + dept);
        log.info("新增部门, dept: {}", dept);
        deptService.add(dept);
        return Result.success();
    }


    /**
     * 根据ID查询部门 - GET http://localhost:8080/depts/1
     */
    //如果路径参数名与controller方法形参名称一致，@PathVariable注解的value属性是可以省略的。
    //@GetMapping("/depts/{id}")
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        //System.out.println("根据ID查询部门: " + id);
        log.info("根据ID查询, id: {}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }


    /**
     * 修改部门 - PUT http://localhost:8080/depts  请求参数：{"id":1,"name":"研发部"}
     */
    //@PutMapping("/depts")
    @PutMapping
    @LogOperation
    public Result update(@RequestBody Dept dept) {
        //System.out.println("修改部门, dept=" + dept);
        log.info("修改部门, dept: {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
