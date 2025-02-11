package com.nju.edu;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionManager {

    private final WalManager walManager = new WalManager();

    private final LockManager lockManager = new LockManager();

    /**
     * key: 事务id
     * value: 事务状态 (ACTIVE, COMMITTED, ROLLED_BACK)
     */
    private final Map<Integer, TransactionStatus> transactionStatuses = new ConcurrentHashMap<>();

    /**
     * key: 事务id
     * value: 该事务读取数据的结果
     */
    private final Map<Integer, Data> snapshots = new ConcurrentHashMap<>();

    private static final AtomicInteger transactionId = new AtomicInteger(-1);

    /**
     * 开始一个新事物
     * @return 该事物的id
     */
    public int beginTransaction() {
        int id = transactionId.incrementAndGet();
        transactionStatuses.put(id, TransactionStatus.ACTIVE);
        walManager.beginTransaction(id);
        return id;
    }

    /**
     * 更新数据 记录日志
     * @param transactionId 事务id
     * @param versionChain versionChain
     * @param newObject newObject
     * @return 本次更新操作后，新数据的版本号
     */
    public int performUpdate(int transactionId, VersionChain versionChain, Object newObject) {
        String resourceId = versionChain.getResourceId();
        String operation = "UPDATE " + resourceId + " to " + newObject;
        walManager.logOperation(transactionId, operation);
        lockManager.acquireLock(resourceId, transactionId);
        // TODO 在上锁和释放锁之间，实现修改数据
        lockManager.releaseLock(resourceId, transactionId);
        // TODO 返回本次更新操作后，新数据的版本号
        return -1;
    }

    public boolean snapshotsContainsTransaction(int transactionId) {
        return snapshots.containsKey(transactionId);
    }

    /**
     * 为某事务看到的 Data 建立快照
     * @param data Data
     * @param transactionId 事务id
     */
    public void takeSnapshot(int transactionId, Data data) {
        // TODO
    }

    /**
     * 根据事务id返回该事务此时应该看到的 Data
     * @param transactionId 事务id
     * @return Data
     */
    public Data getDataByTransactionId(int transactionId) {
        // TODO
        return null;
    }

    /**
     * 本方法用于修改数据的事务提交
     * 提交事务，日志刷盘
     * @param transactionId transactionId
     * @param versionChain versionChain
     * @param version 事务最后一次修改完数据后，新数据的版本号
     */
    public void commitTransaction(int transactionId, VersionChain versionChain, int version) {
        if (transactionStatuses.get(transactionId) == TransactionStatus.ACTIVE) {
            // TODO
        }
        else {
            System.out.println("该事务已提交或回滚");
        }
    }

    /**
     * 本方法用于不修改数据的事务提交
     * 提交事务，日志刷盘
     * @param transactionId transactionId
     */
    public void commitTransaction(int transactionId) {
        if (transactionStatuses.get(transactionId) == TransactionStatus.ACTIVE) {
            // TODO
        }
        else {
            System.out.println("该事务已提交或回滚");
        }
    }

    /**
     * 回滚事务，日志刷盘
     * @param transactionId transactionId
     */
    public void rollbackTransaction(int transactionId) {
        if (transactionStatuses.get(transactionId) == TransactionStatus.ACTIVE) {
            // TODO
            // 思考：在本mvcc模拟实验中，回滚时是否需要按照操作日志进行反向操作，为什么？
        }
        else {
            System.out.println("该事务已提交或回滚");
        }
    }

    public List<String> getLogs(int transactionId) {
        return walManager.getLogs(transactionId);
    }

    public TransactionStatus getStatus(int transactionId) {
        return transactionStatuses.get(transactionId);
    }

    public boolean isActive(int transactionId) {
        return getStatus(transactionId) == TransactionStatus.ACTIVE;
    }
}

