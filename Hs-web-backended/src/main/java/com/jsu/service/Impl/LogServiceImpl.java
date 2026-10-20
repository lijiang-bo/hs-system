package com.jsu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        PageHelper.startPage(logQueryParam.getPage(), logQueryParam.getPageSize());

        List<OperateLog> logList = logMapper.page(logQueryParam);

        PageInfo<OperateLog> pageInfo = new PageInfo<>(logList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
