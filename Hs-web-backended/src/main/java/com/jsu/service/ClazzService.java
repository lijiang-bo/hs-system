package com.jsu.service;

import com.jsu.pojo.Clazz;
import com.jsu.pojo.ClazzQueryParam;
import com.jsu.pojo.PageResult;

import java.util.List;

public interface ClazzService {

    /**
     * 分页查询班级
     *
     * @param clazzQueryParam 查询条件
     * @return 分页查询结果
     */
    PageResult page(ClazzQueryParam clazzQueryParam);


    /**
     * 添加班级
     *
     * @param clazz 班级信息
     */
    void save(Clazz clazz);

    /**
     * 根据ID查询班级
     *
     * @param id 班级ID
     * @return 班级信息
     */
    Clazz getById(Integer id);

    /**
     * 更新班级信息
     *
     * @param clazz 班级信息
     */
    void update(Clazz clazz);

    /**
     * 删除班级
     *
     * @param id 班级ID
     */
    void delete(Integer id);

    /**
     * 查询所有班级
     *
     * @return 班级列表
     */
    List<Clazz> findAll();
}
