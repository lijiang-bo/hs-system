package com.jsu.mapper;


import com.jsu.pojo.Emp;
import com.jsu.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 员工Mapper接口
 */
@Mapper
public interface EmpMapper {

    /**
     * 根据查询条件查询员工列表
     *
     * @param empQueryParam 查询条件
     * @return 员工列表
     */
    List<Emp> list(EmpQueryParam empQueryParam);

    /**
     * 添加员工
     *
     * @param emp 员工信息
     */
    void insert(Emp emp);

    /**
     * 批量删除员工
     *
     * @param ids 员工id列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据id查询员工的详细信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    Emp getById(Integer id);

    /**
     * 修改员工信息
     *
     * @param emp 员工信息
     */
    void updateById(Emp emp);

    /**
     * 统计各个职位的员工人数
     */
    List<Map<String, Object>> countEmpJobData();

    /**
     * 统计员工的性别分布
     */
    List<Map<String, Object>> countEmpGenderData();

    /**
     * 查询全部员工
     *
     * @return 员工列表
     */
    List<Emp> findAll();

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUsernameAndPassword(Emp emp);

    /**
     * 根据用户名查询员工
     */
    Emp getByUsername(String username);

}
