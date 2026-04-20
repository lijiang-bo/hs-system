package com.jsu.mapper;

import com.jsu.pojo.Clazz;
import com.jsu.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClazzMapper {


    /**
     * 分页查询班级
     *
     * @param clazzQueryParam 查询条件
     * @return 分页查询结果
     */
    List<Clazz> page(ClazzQueryParam clazzQueryParam);

    /**
     * 添加班级
     *
     * @param clazz 班级信息
     */
    void insertClazz(Clazz clazz);

    /**
     * 根据ID查询班级
     *
     * @param id 班级ID
     * @return 班级信息
     */
    Clazz selectById(Long id);

    /**
     * 更新班级信息
     *
     * @param clazz 班级信息
     */
    void updateClazz(Clazz clazz);

    /**
     * 检查班级下是否有学生
     *
     * @param clazzId 班级ID
     * @return 学生数量
     */
    Integer countStudentByClazzId(Long clazzId);

    /**
     * 删除班级
     *
     * @param id 班级ID
     */
    void deleteById(Long id);

    /**
     * 查询所有班级
     *
     * @return 班级列表
     */
    List<Clazz> findAll();
}
