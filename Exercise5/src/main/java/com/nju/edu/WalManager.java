package com.nju.edu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalManager {
    /**
     * key: 事务id
     * value: 事务操作列表
     */
    private final Map<Integer, List<String>> transactionLogs = new HashMap<>();

    /**
     * 某事务开始时，进行该事务的日志初始化
     * @param transactionId 事务id
     */
    public void beginTransaction(int transactionId) {
        // TODO
    }

    /**
     * 记录事务的某次操作
     * @param transactionId 事务id
     * @param operation 操作
     */
    public void logOperation(int transactionId, String operation) {
        // TODO
    }

    public List<String> getLogs(int transactionId) {
        return transactionLogs.get(transactionId);
    }

    public void commitTransaction(int transactionId) {
        flushLog(transactionId);
        transactionLogs.remove(transactionId);
    }

    private void flushLog(int transactionId) {
        // 将事务日志写入磁盘
        // 这里使用简化的控制台输出模拟磁盘写入
        System.out.println("Flushing transaction " + transactionId + " to disk:");
        for (String log : transactionLogs.get(transactionId)) {
            System.out.println(log);
        }
    }

    public void rollbackTransaction(int transactionId) {
        flashRollBackLog(transactionId);
        transactionLogs.remove(transactionId);
    }

    private void flashRollBackLog(int transactionId) {
        // 将回滚日志写入磁盘
        // 这里使用简化的控制台输出模拟磁盘写入
        System.out.println("Flushing roll back transaction " + transactionId + " to disk:");
        for (String log : transactionLogs.get(transactionId)) {
            System.out.println(log);
        }
    }
}
