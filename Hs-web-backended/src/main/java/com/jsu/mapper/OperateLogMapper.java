package com.jsu.mapper;

import com.jsu.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("INSERT INTO operate_log " +
            "(operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time, operate_emp_name) " +
            "VALUES " +
            "(#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime}, #{operateEmpName})")
    void insert(OperateLog log);

}

