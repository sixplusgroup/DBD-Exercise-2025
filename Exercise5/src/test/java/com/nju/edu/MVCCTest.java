package com.nju.edu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MVCCTest {

    private final Data data = new Data(20);
    private VersionChain versionChain;
    private TransactionManager transactionManager;
    private ReadWriteHandler readWriteHandler;

    @BeforeEach
    public void init() {
        versionChain = new VersionChain(data);
        transactionManager = new TransactionManager();
        readWriteHandler = new ReadWriteHandler(transactionManager);
    }

    @Test
    void testRead() {
        // 开始事务
        int transactionId = transactionManager.beginTransaction();
        // 读取数据
        Assertions.assertEquals(20, readWriteHandler.read(versionChain, transactionId));
        // 提交事务
        transactionManager.commitTransaction(transactionId);
        // 查看最新正确版本号
        Assertions.assertEquals(0, versionChain.getNewestRightVersion());
    }

    @Test
    void testWrite() {
        // 开始事务
        int transactionId = transactionManager.beginTransaction();
        // 读取数据
        int n = (int) readWriteHandler.read(versionChain, transactionId);
        Assertions.assertEquals(20, n);
        // 执行数据更新操作
        int version = readWriteHandler.write(transactionId, versionChain, n + 1);
        Assertions.assertEquals(1, version);
        // 读取数据
        n = (int) readWriteHandler.read(versionChain, transactionId);
        Assertions.assertEquals(21, n);
        // 再次执行数据更新操作
        version = readWriteHandler.write(transactionId, versionChain, n + 11);
        // 读取数据
        n = (int) readWriteHandler.read(versionChain, transactionId);
        Assertions.assertEquals(32, n);
        // 提交事务
        transactionManager.commitTransaction(transactionId, versionChain, version);
        // 查看最新正确版本号
        Assertions.assertEquals(2, versionChain.getNewestRightVersion());
    }

    @Test
    void testLog() {
        // 开始事务
        int transactionId = transactionManager.beginTransaction();
        // 执行两次数据更新操作
        int version = readWriteHandler.write(transactionId, versionChain, 21);
        version = readWriteHandler.write(transactionId, versionChain, 23);
        // 获取日志
        List<String> logs = transactionManager.getLogs(transactionId);
        Assertions.assertEquals("UPDATE " + versionChain.getResourceId() + " to " + 21, logs.get(0));
        Assertions.assertEquals("UPDATE " + versionChain.getResourceId() + " to " + 23, logs.get(1));
        // 提交事务
        transactionManager.commitTransaction(transactionId, versionChain, version);
    }

    @Test
    void testStatus() {
        // 开始事务1
        int transactionId1 = transactionManager.beginTransaction();
        // 事务1执行数据更新操作
        int v1 = readWriteHandler.write(transactionId1, versionChain, 21);
        // 事务1回滚
        transactionManager.rollbackTransaction(transactionId1);
        // 开始事务2
        int transactionId2 = transactionManager.beginTransaction();
        // 事务2执行数据更新操作
        int v2 = readWriteHandler.write(transactionId2, versionChain, 21);
        // 提交事务2
        transactionManager.commitTransaction(transactionId2, versionChain, v2);
        // 查看事务状态
        Assertions.assertEquals(TransactionStatus.ROLLED_BACK, transactionManager.getStatus(transactionId1));
        Assertions.assertEquals(TransactionStatus.COMMITTED, transactionManager.getStatus(transactionId2));
    }

    @Test
    void testDirtyRead() {
        // 开始事务1
        int transactionId1 = transactionManager.beginTransaction();
        // 开始事务2
        int transactionId2 = transactionManager.beginTransaction();
        // 事务1读取数据
        int n1 = (int) readWriteHandler.read(versionChain, transactionId1);
        Assertions.assertEquals(20, n1);
        // 事务1执行数据更新操作
        int v1 = readWriteHandler.write(transactionId1, versionChain, n1 + 1);
        Assertions.assertEquals(1, v1);
        // 事务1读取数据
        n1 = (int) readWriteHandler.read(versionChain, transactionId1);
        Assertions.assertEquals(21, n1);
        // 事务2读取数据
        int n2 = (int) readWriteHandler.read(versionChain, transactionId2);
        Assertions.assertEquals(20, n2);
        // 事务1回滚
        transactionManager.rollbackTransaction(transactionId1);
        // 事务2读取数据
        n2 = (int) readWriteHandler.read(versionChain, transactionId2);
        Assertions.assertEquals(20, n2);
        // 提交事务2
        transactionManager.commitTransaction(transactionId2);
        // 查看最新正确版本号
        Assertions.assertEquals(0, versionChain.getNewestRightVersion());
    }

    @Test
    void testUnrepeatableRead() {
        // 开始事务1
        int transactionId1 = transactionManager.beginTransaction();
        // 开始事务2
        int transactionId2 = transactionManager.beginTransaction();
        // 事务1读取数据
        int n1 = (int) readWriteHandler.read(versionChain, transactionId1);
        Assertions.assertEquals(20, n1);
        // 事务2读取数据
        int n2 = (int) readWriteHandler.read(versionChain, transactionId2);
        Assertions.assertEquals(20, n2);
        // 事务1执行数据更新操作
        int v1 = readWriteHandler.write(transactionId1, versionChain, n1 + 1);
        Assertions.assertEquals(1, v1);
        // 事务2读取数据
        Assertions.assertEquals(n2, (int) readWriteHandler.read(versionChain, transactionId2));
        // 提交事务1
        transactionManager.commitTransaction(transactionId1, versionChain, v1);
        // 提交事务2
        transactionManager.commitTransaction(transactionId2);
        // 查看最新正确版本号
        Assertions.assertEquals(1, versionChain.getNewestRightVersion());
    }

    @Test
    void testLostToModify() {
        // 开始事务1
        int transactionId1 = transactionManager.beginTransaction();
        // 开始事务2
        int transactionId2 = transactionManager.beginTransaction();
        // 事务1读取数据
        int n1 = (int) readWriteHandler.read(versionChain, transactionId1);
        Assertions.assertEquals(20, n1);
        // 事务2读取数据
        int n2 = (int) readWriteHandler.read(versionChain, transactionId2);
        Assertions.assertEquals(20, n2);
        // 事务1执行数据更新操作
        int v1 = readWriteHandler.write(transactionId1, versionChain, n1 + 1);
        Assertions.assertEquals(1, v1);
        // 事务2执行数据更新操作
        int v2 = readWriteHandler.write(transactionId2, versionChain, n1 + 1);
        Assertions.assertEquals(2, v2);
        // 事务1读取数据
        n1 = (int) readWriteHandler.read(versionChain, transactionId1);
        Assertions.assertEquals(21, n1);
        // 事务2读取数据
        n2 = (int) readWriteHandler.read(versionChain, transactionId2);
        Assertions.assertEquals(21, n2);
        // 提交事务1
        transactionManager.commitTransaction(transactionId1, versionChain, v1);
        // 提交事务2
        transactionManager.commitTransaction(transactionId2, versionChain, v2);
        // 查看最新正确版本号
        Assertions.assertEquals(2, versionChain.getNewestRightVersion());
        System.out.println("事务1的修改丢失");
    }
}
