package com.jsu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDiscipline {

    private Integer id; //违纪记录ID

    private Integer studentId; //学员ID，关联student表

    private String studentName; //学员姓名（冗余）

    private String disciplineType; //违纪类型：迟到/旷课/早退/玩手机/打架等

    private String disciplineContent; //违纪详情描述

    private LocalDateTime disciplineTime; //违纪发生时间

    private LocalDateTime createTime; //记录创建时间

    private LocalDateTime updateTime; //修改时间
}
