package com.jsu.util;

/**
 * ThreadLocal工具类，用于存储和获取当前登录员工ID
 */
public class CurrentHolder {

    private static final ThreadLocal<Integer> CURRENT_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前登录员工ID
     *
     * @param employeeId 员工ID
     */
    public static void setCurrentId(Integer employeeId) {
        CURRENT_LOCAL.set(employeeId);
    }

    /**
     * 获取当前登录员工ID
     *
     * @return 员工ID
     */
    public static Integer getCurrentId() {
        return CURRENT_LOCAL.get();
    }

    /**
     * 移除当前线程绑定的员工ID
     */
    public static void remove() {
        CURRENT_LOCAL.remove();
    }
}
