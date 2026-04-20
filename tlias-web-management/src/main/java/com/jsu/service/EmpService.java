package com.jsu.service;

import com.jsu.pojo.Emp;
import com.jsu.pojo.EmpQueryParam;
import com.jsu.pojo.LoginInfo;
import com.jsu.pojo.PageResult;

import java.util.List;

public interface EmpService {
    /**
     * 分页查询员工
     *
     * @param empQueryParam 查询条件
     * @return 分页查询结果
     */
    PageResult page(EmpQueryParam empQueryParam);

    /**
     * 添加员工
     *
     * @param emp 员工信息
     */
    void save(Emp emp);

    /**
     * 批量删除员工
     *
     * @param ids 员工id列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 查询回显
     *
     * @param id 员工id
     * @return 员工信息
     */
    Emp getInfo(Integer id);

    /**
     * 修改员工信息
     *
     * @param emp 员工信息
     */
    void update(Emp emp);

    /**
     * 查询全部员工
     *
     * @return 员工列表
     */
    List<Emp> findAll();

    /**
     * 员工登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 员工信息
     */
    LoginInfo login(Emp emp);
}
