package com.jsu.exception;

/**
 * 班级删除异常 - 班级下有学生时不允许删除
 */
public class ClazzDeleteException extends RuntimeException {

    public ClazzDeleteException() {
        super("对不起, 该班级下有学生, 不能直接删除");
    }
}
