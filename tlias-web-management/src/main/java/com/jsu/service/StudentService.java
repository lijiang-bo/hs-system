package com.jsu.service;

import com.jsu.pojo.PageResult;
import com.jsu.pojo.Student;
import com.jsu.pojo.StudentQueryParam;
import com.jsu.pojo.Violation;

public interface StudentService {

    /**
     * 分页查询学生
     *
     * @param studentQueryParam 查询参数
     * @return 分页查询结果
     */
    PageResult page(StudentQueryParam studentQueryParam);

    /**
     * 添加学生
     *
     * @param student 学生信息
     */
    void save(Student student);

    /**
     * 根据id查询学生
     *
     * @param id 学生id
     * @return 学生信息
     */
    Student getById(Long id);

    /**
     * 修改学生信息
     *
     * @param student 学生信息
     */
    void update(Student student);

    /**
     * 删除学生信息
     *
     * @param id 学生id
     */
    void delete(Long id);

    /**
     * 违纪处理
     *
     * @param violation 违纪信息
     */
    void violation(Violation violation);
}
