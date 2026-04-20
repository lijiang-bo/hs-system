package com.jsu.aspect;

import com.alibaba.fastjson2.JSON;
import com.jsu.anno.LogOperation;
import com.jsu.mapper.EmpMapper;
import com.jsu.mapper.OperateLogMapper;
import com.jsu.pojo.Emp;
import com.jsu.pojo.OperateLog;
import com.jsu.util.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 日志切面类
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Autowired
    private EmpMapper empMapper;

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation log) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行方法
        Object result = joinPoint.proceed();

        // 当前时间
        long endTime = System.currentTimeMillis();

        // 耗时
        long costTime = endTime - startTime;

        // 构建日志对象
        OperateLog operateLog = new OperateLog();
        Integer currentUserId = getCurrentUserId();
        operateLog.setOperateEmpId(currentUserId);
        Emp currentEmp = currentUserId == null ? null : empMapper.getById(currentUserId);
        operateLog.setOperateEmpName(currentEmp == null ? null : currentEmp.getName());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(JSON.toJSONString(joinPoint.getArgs()));
        operateLog.setReturnValue(JSON.toJSONString(result));
        operateLog.setCostTime(costTime);

        // 插入日志
        operateLogMapper.insert(operateLog);

        return result;
    }

    /**
     * 从ThreadLocal中获取当前用户ID
     */
    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}

