package com.jsu.service;

import com.jsu.pojo.LogQueryParam;
import com.jsu.pojo.PageResult;

public interface LogService {

    /**
     * 分页查询日志信息
     *
     * @param logQueryParam 查询参数
     * @return 分页查询结果
     */
    PageResult page(LogQueryParam logQueryParam);
}
