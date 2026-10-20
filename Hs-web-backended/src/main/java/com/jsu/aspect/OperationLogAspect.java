package com.jsu.aspect;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jsu.anno.LogOperation;
import com.jsu.mapper.OperateLogMapper;
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

    @Around("@annotation(logAnno)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation logAnno) throws Throwable {
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
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setCostTime(costTime);

        // 日志归档（含序列化/脱敏/入库），失败不影响业务
        try {
            operateLog.setOperateEmpName(CurrentHolder.getCurrentName());
            operateLog.setMethodParams(maskSensitiveParams(joinPoint.getArgs()));
            operateLog.setReturnValue(JSON.toJSONString(result));
            operateLogMapper.insert(operateLog);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }

        return result;
    }

    /**
     * 脱敏敏感参数
     * 将参数中所有 password 字段替换为 ******
     */
    private String maskSensitiveParams(Object[] args) {
        String json = JSON.toJSONString(args);
        JSONArray array = JSON.parseArray(json);
        for (int i = 0; i < array.size(); i++) {
            Object obj = array.get(i);
            if (obj instanceof JSONObject) {
                maskJsonObject((JSONObject) obj);
            }
        }
        return array.toJSONString();
    }

    private void maskJsonObject(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            if ("password".equals(key)) {
                jsonObject.put(key, "******");
            } else {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    maskJsonObject((JSONObject) value);
                }
            }
        }
    }

    /**
     * 从ThreadLocal中获取当前用户ID
     */
    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}
