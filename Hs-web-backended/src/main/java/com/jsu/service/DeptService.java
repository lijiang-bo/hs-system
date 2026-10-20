package com.jsu.service;

import com.jsu.pojo.Dept;

import java.util.List;

public interface DeptService {

    /**
     * 查询全部部门数据
     *
     */
    List<Dept> findAll();

    /**
     * 根据ID删除部门
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 添加新增部门
     *
     * @param dept
     */
    void add(Dept dept);

    /**
     * 根据ID查询部门
     *
     * @param id
     * @return
     */
    Dept getById(Integer id);

    /**
     * 修改部门
     *
     * @param dept
     */
    void update(Dept dept);
}
