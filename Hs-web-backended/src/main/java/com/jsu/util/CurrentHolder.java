package com.jsu.util;

/**
 * ThreadLocal工具类，用于存储和获取当前登录员工ID、角色及姓名
 */
public class CurrentHolder {

    private static final ThreadLocal<Integer> CURRENT_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Integer> ROLE_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<String> NAME_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前登录员工ID
     */
    public static void setCurrentId(Integer employeeId) {
        CURRENT_LOCAL.set(employeeId);
    }

    /**
     * 获取当前登录员工ID
     */
    public static Integer getCurrentId() {
        return CURRENT_LOCAL.get();
    }

    /**
     * 设置当前登录员工角色
     */
    public static void setCurrentRole(Integer role) {
        ROLE_LOCAL.set(role);
    }

    /**
     * 获取当前登录员工角色
     */
    public static Integer getCurrentRole() {
        return ROLE_LOCAL.get();
    }

    /**
     * 设置当前登录员工姓名
     */
    public static void setCurrentName(String name) {
        NAME_LOCAL.set(name);
    }

    /**
     * 获取当前登录员工姓名
     */
    public static String getCurrentName() {
        return NAME_LOCAL.get();
    }

    /**
     * 移除当前线程绑定的数据
     */
    public static void remove() {
        CURRENT_LOCAL.remove();
        ROLE_LOCAL.remove();
        NAME_LOCAL.remove();
    }
}
