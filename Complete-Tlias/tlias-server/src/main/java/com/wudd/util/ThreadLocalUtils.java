package com.wudd.util;

public class ThreadLocalUtils {
    private static ThreadLocal<String> nameThreadLocal = new ThreadLocal<>();

    public static void setName(String name) {
        nameThreadLocal.set(name);
    }

    public static String getName() {
        return nameThreadLocal.get();
    }

    public static void clear() {
        nameThreadLocal.remove();
    }
}
