package com.jsu.mapper;

import com.jsu.pojo.LogQueryParam;
import com.jsu.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    /**
     * 分页查询日志信息
     *
     * @param logQueryParam 查询参数
     * @return 日志列表
     */
    List<OperateLog> page(LogQueryParam logQueryParam);

}
