package org.example;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, String> dataStore = new HashMap<>();

    // 获取数据
    public static String getData(String key) {
        return dataStore.get(key);
    }

    // 存储数据
    public static void putData(String key, String value) {
        dataStore.put(key, value);
    }

    //删除数据
    public static void deleteData(String key) {
        dataStore.remove(key);
    }

    //模拟数据库崩溃
    public static void getDown() {
        dataStore.clear();
    }

    // 打印所有数据
    public static void printData() {
        System.out.println("当前数据库数据: " + dataStore);
    }

}

