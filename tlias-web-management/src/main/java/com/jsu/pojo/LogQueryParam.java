package com.jsu.pojo;

import lombok.Data;

@Data
public class LogQueryParam {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页显示数量
     */
    private Integer pageSize = 10;
}
