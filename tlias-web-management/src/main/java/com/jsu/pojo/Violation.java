package com.jsu.pojo;

import lombok.Data;

/**
 * 违纪处理请求参数
 */
@Data
public class Violation {
    private Long id;      // 学员ID
    private Integer score;  // 扣除分数
}
