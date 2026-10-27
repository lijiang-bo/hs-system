package com.jsu.pojo;

import lombok.Data;

@Data
public class StudentDisciplineQueryParam {

    /**
     * 当前页码，设置默认参数
     */
    private Integer page = 1;

    /**
     * 每页显示数量，设置默认参数
     */
    private Integer pageSize = 10;

    /**
     * 学员姓名（模糊查询）
     */
    private String studentName;

    /**
     * 违纪类型
     */
    private String disciplineType;

    /**
     * 违纪开始时间（前端传 'yyyy-MM-dd HH:mm:ss'）
     */
    private String startTime;

    /**
     * 违纪结束时间
     */
    private String endTime;
}
