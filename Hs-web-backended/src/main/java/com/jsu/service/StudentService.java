package com.jsu.service;

import com.jsu.pojo.PageResult;
import com.jsu.pojo.Student;
import com.jsu.pojo.StudentQueryParam;

public interface StudentService {

    /**
     * 分页查询学生
     */
    PageResult page(StudentQueryParam studentQueryParam);

    /**
     * 添加学生
     */
    void save(Student student);

    /**
     * 根据id查询学生
     */
    Student getById(Integer id);

    /**
     * 修改学生信息
     */
    void update(Student student);

    /**
     * 删除学生信息
     */
    void delete(Integer id);

    /**
     * 违纪处理
     *
     * @param id    学员ID
     * @param score 扣除分数
     */
    void violation(Integer id, Integer score);
}
