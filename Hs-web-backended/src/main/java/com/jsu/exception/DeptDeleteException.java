package com.jsu.exception;

/**
 * 部门删除异常 - 部门下有员工时不允许删除
 */
public class DeptDeleteException extends RuntimeException {

    public DeptDeleteException() {
        super("对不起，当前部门下有员工，不能直接删除！");
    }
}
