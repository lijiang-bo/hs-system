package com.jsu.pojo;


import lombok.Data;

@Data
public class StudentQueryParam {
    private String name;
    private String degree;
    private String clazzId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
