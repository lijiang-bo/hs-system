package com.jsu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartOption {

    /**
     * 分类名称列表（如职位名称、班级名称）
     */
    private List categories;

    /**
     * 数值列表
     */
    private List values;
}
