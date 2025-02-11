package com.nju.edu;

public class ReadWriteHandler {
    private final TransactionManager transactionManager;

    public ReadWriteHandler(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 如果是该事务首次读取，建立快照
     * 否则，从快照中读取
     * @param versionChain versionChain
     * @param transactionId transactionId
     * @return 读取的数据内容
     */
    public Object read(VersionChain versionChain, int transactionId) {
        // TODO
        return null;
    }

    public int write(int transactionId, VersionChain versionChain, Object newObject) {
        return transactionManager.performUpdate(transactionId, versionChain, newObject);
    }
}
