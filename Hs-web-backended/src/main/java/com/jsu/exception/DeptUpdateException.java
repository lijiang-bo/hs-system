package com.jsu.exception;

/**
 * 部门更新异常 - 部门不存在时不允许更新
 */
public class DeptUpdateException extends RuntimeException {

    public DeptUpdateException(Integer id) {
        super("对不起，ID为 " + id + " 的部门不存在，无法更新！");
    }
}
