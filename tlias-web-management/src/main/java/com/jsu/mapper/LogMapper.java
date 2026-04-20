package com.jsu.mapper;

import com.jsu.pojo.LogQueryParam;
import com.jsu.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
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

    /**
     * 统计日志总数
     *
     * @return 总记录数
     */
    Long count();

    /**
     * 保存操作日志
     *
     * @param operateLog 操作日志
     */
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time, operate_emp_name) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime}, #{operateEmpName})")
    void save(OperateLog operateLog);
}
