package com.jsu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jsu.mapper.LogMapper;
import com.jsu.pojo.LogQueryParam;
import com.jsu.pojo.OperateLog;
import com.jsu.pojo.PageResult;
import com.jsu.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public PageResult page(LogQueryParam logQueryParam) {
        // 1. 设置PageHelper分页参数
        PageHelper.startPage(logQueryParam.getPage(), logQueryParam.getPageSize());

        // 2. 执行查询
        List<OperateLog> logList = logMapper.page(logQueryParam);

        // 3. 封装分页结果
        Page<OperateLog> p = (Page<OperateLog>) logList;
        return new PageResult(p.getTotal(), p.getResult());
    }
}
