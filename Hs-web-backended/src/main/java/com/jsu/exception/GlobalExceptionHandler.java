package com.jsu.exception;

import com.jsu.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理班级删除异常
    @ExceptionHandler(ClazzDeleteException.class)
    public Result handleClazzDeleteException(ClazzDeleteException e) {
        log.warn("班级删除异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //处理部门删除异常
    @ExceptionHandler(DeptDeleteException.class)
    public Result handleDeptDeleteException(DeptDeleteException e) {
        log.warn("部门删除异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //处理部门更新异常
    @ExceptionHandler(DeptUpdateException.class)
    public Result handleDeptUpdateException(DeptUpdateException e) {
        log.warn("部门更新异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //处理用户名重复异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("用户名重复: {}", e.getMessage());
        return Result.error("用户名已存在，请更换后重试");
    }

    //处理请求体格式错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求参数格式错误: {}", e.getMessage());
        return Result.error("请求参数格式错误，请检查输入");
    }

    //处理缺少必填参数
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("缺少必填参数: {}", e.getParameterName());
        return Result.error("缺少必填参数: " + e.getParameterName());
    }

    //处理权限不足异常
    @ExceptionHandler(SecurityException.class)
    public Result handleSecurityException(SecurityException e) {
        log.warn("权限不足: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //处理异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(e.getMessage());
    }
}
