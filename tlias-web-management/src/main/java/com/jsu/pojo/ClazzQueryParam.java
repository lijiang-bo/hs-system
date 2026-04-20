package com.jsu.pojo;


import lombok.Data;

@Data
public class ClazzQueryParam {
    private String name;
    private String begin;
    private String end;
    private Integer page = 1;
    private Integer pageSize = 10;
}
