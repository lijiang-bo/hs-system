package com.jsu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsu.mapper.StudentDisciplineMapper;
import com.jsu.pojo.PageResult;
import com.jsu.pojo.StudentDiscipline;
import com.jsu.pojo.StudentDisciplineQueryParam;
import com.jsu.service.StudentDisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentDisciplineServiceImpl implements StudentDisciplineService {

    @Autowired
    private StudentDisciplineMapper studentDisciplineMapper;

    @Override
    public PageResult page(StudentDisciplineQueryParam studentDisciplineQueryParam) {
        // 1. 启动分页拦截（必须在 mapper 查询之前调用）
        PageHelper.startPage(studentDisciplineQueryParam.getPage(), studentDisciplineQueryParam.getPageSize());
        // 2. 执行查询，返回当前页的 List（PageHelper 已自动加了 LIMIT，并顺便查出总条数）
        List<StudentDiscipline> list = studentDisciplineMapper.page(studentDisciplineQueryParam);
        // 3. 包装成 PageInfo，取出总条数和当前页数据
        PageInfo<StudentDiscipline> pageInfo = new PageInfo<>(list);
        // 4. 转成项目自定义的 PageResult(total, rows)
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
