package com.jsu.service.Impl;

import com.jsu.mapper.EmpMapper;
import com.jsu.mapper.StudentMapper;
import com.jsu.pojo.ChartOption;
import com.jsu.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 统计各个职位的员工人数
     *
     * @return
     */
    @Override
    public ChartOption getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        List<Object> categories = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> values = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new ChartOption(categories, values);
    }

    /**
     * 统计各个性别的员工人数
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    /**
     * 统计各个班级的学生人数
     *
     * @return
     */
    @Override
    public ChartOption getClazzNumData() {
        List<Map<String, Object>> list = studentMapper.countClazzNumData();
        List<Object> categories = list.stream().map(dataMap -> dataMap.get("clazz")).toList();
        List<Object> values = list.stream().map(dataMap -> dataMap.get("value")).toList();
        return new ChartOption(categories, values);
    }

    /**
     * 统计学员的学历分布
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }
    
}
