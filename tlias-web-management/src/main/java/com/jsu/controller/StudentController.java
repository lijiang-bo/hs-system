package com.jsu.controller;

import com.jsu.anno.LogOperation;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Result;
import com.jsu.pojo.Student;
import com.jsu.pojo.StudentQueryParam;
import com.jsu.pojo.Violation;
import com.jsu.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询学生
     *
     * @param studentQueryParam 查询条件
     * @return 分页查询结果
     */
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("分页查询学生,参数:{}", studentQueryParam);
        PageResult pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }


    /**
     * 添加学生信息
     */
    @PostMapping
    @LogOperation
    public Result save(@RequestBody Student student) {
        log.info("添加学生信息, student: {}", student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 根据ID查询学生信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 修改学生信息
     */
    @PutMapping
    @LogOperation
    public Result update(@RequestBody Student student) {
        log.info("修改学生信息, student: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 删除学生信息
     */
    @DeleteMapping("/{id}")
    @LogOperation
    public Result delete(@PathVariable Long id) {
        log.info("删除学生信息, id: {}", id);
        studentService.delete(id);
        return Result.success();
    }


    /**
     * 违纪处理
     */
    @PutMapping("/violation/{id}/{score}")
    @LogOperation
    public Result violation(@PathVariable Long id, @PathVariable Integer score) {
        log.info("违纪处理信息, 学员ID: {}, 扣除分数: {}", id, score);
        Violation violation = new Violation();
        violation.setId(id);
        violation.setScore(score);
        studentService.violation(violation);
        return Result.success();
    }
}
