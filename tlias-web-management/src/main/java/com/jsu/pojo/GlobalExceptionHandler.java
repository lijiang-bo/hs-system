package com.jsu.pojo;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理班级删除异常
    @ExceptionHandler
    public Result ex(ClazzDeleteException e) {
        return Result.error(e.getMessage());
    }

    //处理部门删除异常
    @ExceptionHandler
    public Result ex(DeptDeleteException e) {
        return Result.error(e.getMessage());
    }

    //处理异常
    @ExceptionHandler
    public Result ex(Exception e) {//方法形参中指定能够处理的异常类型
        e.printStackTrace();//打印堆栈中的异常信息
        //捕获到异常之后，响应一个标准的Result
        return Result.error("对不起,操作失败,请联系管理员");
    }

}