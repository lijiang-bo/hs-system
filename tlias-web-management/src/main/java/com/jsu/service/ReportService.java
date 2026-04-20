package com.jsu.service;

import com.jsu.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 统计各个职位的员工人数
     *
     * @return
     */
    JobOption getEmpJobData();

    /**
     * 统计各个性别的员工人数
     *
     * @return
     */
    List<Map> getEmpGenderData();

    /**
     * 统计各个班级的学生人数
     *
     * @return
     */
    JobOption getClazzNumData();

    /**
     * 统计学员的学历分布
     *
     * @return
     */
    List<Map> getStudentDegreeData();
}