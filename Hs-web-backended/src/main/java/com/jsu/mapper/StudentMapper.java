package com.jsu.mapper;


import com.jsu.pojo.Student;
import com.jsu.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 分页查询学生
     *
     * @param studentQueryParam 查询参数
     * @return 学生列表
     */
    List<Student> page(StudentQueryParam studentQueryParam);

    /**
     * 添加学生
     *
     * @param student 学生信息
     */
    void insert(Student student);

    /**
     * 根据id查询学生
     *
     * @param id 学生id
     * @return 学生信息
     */
    Student selectById(Integer id);

    /**
     * 修改学生信息
     *
     * @param student 学生信息
     */
    void updateById(Student student);

    /**
     * 删除学生信息
     *
     * @param id 学生id
     */
    void deleteById(Integer id);

    /**
     * 违纪处理 - 增加违纪次数和扣分
     *
     * @param id 学生id
     * @param score 扣除分数
     */
    void updateViolation(@Param("id") Integer id, @Param("score") Integer score);

    /**
     * 统计学员的学历分布
     *
     * @return 学历统计列表
     */
    List<Map<String, Object>> countStudentDegreeData();

    /**
     * 统计每个班级的学生人数
     *
     * @return 班级人数统计列表
     */
    List<Map<String, Object>> countClazzNumData();
}
