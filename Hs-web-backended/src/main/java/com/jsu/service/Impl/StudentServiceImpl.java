package com.jsu.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsu.mapper.StudentMapper;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.Student;
import com.jsu.pojo.StudentQueryParam;
import com.jsu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;


    /**
     * 分页查询学生
     *
     * @param studentQueryParam 查询参数
     * @return 分页查询结果
     */
    @Override
    public PageResult page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());

        List<Student> studentList = studentMapper.page(studentQueryParam);

        PageInfo<Student> pageInfo = new PageInfo<>(studentList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }


    /**
     * 添加学生
     *
     * @param student 学生信息
     */
    @Override
    public void save(Student student) {
        studentMapper.insert(student);
    }

    /**
     * 根据id查询学生
     *
     * @param id 学生id
     * @return 学生信息
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.selectById(id);
    }

    /**
     * 修改学生信息
     *
     * @param student 学生信息
     */
    @Override
    public void update(Student student) {
        studentMapper.updateById(student);
    }

    /**
     * 删除学生信息
     *
     * @param id 学生id
     */
    @Override
    public void delete(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 违纪处理
     *
     * @param id    学员ID
     * @param score 扣除分数
     */
    @Override
    public void violation(Integer id, Integer score) {
        studentMapper.updateViolation(id, score);
    }
}
