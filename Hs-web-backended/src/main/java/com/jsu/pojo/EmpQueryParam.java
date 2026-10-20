package com.jsu.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 员工查询条件
 */
@Data
public class EmpQueryParam {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页显示数量
     */
    private Integer pageSize = 10;

    /**
     * 员工姓名（模糊查询）
     */
    private String name;

    /**
     * 员工性别
     */
    private Integer gender;

    /**
     * 入职日期开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    /**
     * 入职日期结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    /**
     * 角色
     */
    private Integer role;
}
