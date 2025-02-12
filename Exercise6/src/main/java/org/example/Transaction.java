package org.example;

import org.example.LogFile.UndoLog;
import org.example.LogFile.RedoLog;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    //事务的唯一标识
    private String transactionId;

    //表示事务是否处于活动状态。注意：事务只有在回滚或提交后才会结束，保存点回滚不会结束。
    private boolean isActive;

    //日志管理器，用于记录日志
    private WAL wal;

    //TODO
    //你需要自行设计数据结构以存储Savapoint
    private List<Integer> savepoints;

    public Transaction(String transactionId, WAL wal) {
        this.transactionId = transactionId;
        this.isActive = true;
        this.savepoints = new ArrayList<>();
        this.wal = wal;
    }

    /**
     * TODO
     * 事务中单个操作的执行，你需要实现INSERT、UPDATE、DELETE三种类型操作
     * 先通过WAL中的相应方法将操作记录到Redo、Undo日志中
     * 而后在Database中进行数据变更
     *
     * @param operation
     * @throws Exception
     */
    public void execute(Operation operation) throws Exception {
        if (!isActive) {
            throw new Exception("事务已结束，无法执行操作");
        }
        switch (operation.getType()) {
            case "INSERT":
                // 插入数据到数据库
                Database.putData(operation.getKey(), operation.getValue());
                // 记录 LogFile.UndoLog: 记录删除操作（因为需要在回滚时删除刚插入的数据）
                wal.logUndo(transactionId, "DELETE", operation.getKey(), null);  // 插入操作的回滚是删除
                // 记录 LogFile.RedoLog: 记录插入操作
                wal.logRedo(transactionId, "INSERT", operation.getKey(), operation.getValue());
                break;
            case "UPDATE":
                // 获取旧值并更新数据库
                String oldValue = Database.getData(operation.getKey());
                Database.putData(operation.getKey(), operation.getValue());
                // 记录 LogFile.UndoLog: 保存旧值，以便回滚
                wal.logUndo(transactionId, "UPDATE", operation.getKey(), oldValue);
                // 记录 LogFile.RedoLog: 记录更新操作
                wal.logRedo(transactionId, "UPDATE", operation.getKey(), operation.getValue());
                break;
            case "DELETE":
                // 获取旧值并删除数据
                String valueToDelete = Database.getData(operation.getKey());
                Database.deleteData(operation.getKey());  // 删除数据
                // 记录 LogFile.UndoLog: 保存旧值，以便回滚
                wal.logUndo(transactionId, "INSERT", operation.getKey(), valueToDelete);
                // 记录 LogFile.RedoLog: 记录删除操作
                wal.logRedo(transactionId, "DELETE", operation.getKey(), null);
                break;
        }
    }


    /**
     * TODO
     * 处理该事务的回滚
     * 你需要利用WAL获取所需要的Undo日志记录并进行回滚
     *
     * @throws Exception
     */
    public void rollback() throws Exception {
        if (!isActive) {
            throw new Exception("事务已提交或回滚");
        }

        // 回滚所有操作
        List<UndoLog> logs = wal.getUndoLogs();
        int size = logs.size();
        for (int i = size - 1; i >= 0; i--) {
            UndoLog log = logs.get(i);
            if (!log.getTransactionId().equals(this.transactionId)) {
                break;
            }
            switch (log.getOperationType()) {
                case "INSERT":
                    Database.putData(log.getKey(), log.getOldValue());
                    break;
                case "UPDATE":
                    Database.putData(log.getKey(), log.getOldValue());
                    break;
                case "DELETE":
                    Database.deleteData(log.getKey());
                    break;
            }
        }

        isActive = false;
    }

    /**
     * TODO
     * 设置一个保存点
     * 你需要自行设计数据结构以保存savepoint
     *
     * @throws Exception
     */
    public void setSavepoint() throws Exception {
        if (!isActive) {
            throw new Exception("事务已提交或回滚，无法提交保存点");
        }
        savepoints.add(wal.getUndoLogs().size());
    }

    /**
     * TODO
     * 回滚到最近的一个保存点
     * 回滚到保存点不会终止事务
     *
     * @throws Exception
     */
    public void rollbackToSavepoint() throws Exception {
        if (!isActive) {
            throw new Exception("事务已提交或回滚");
        }

        int savepointIndex = savepoints.remove(savepoints.size() - 1);
        List<UndoLog> logs = wal.getUndoLogs();
        int size = logs.size();
        for (int i = size - 1; i >= savepointIndex; i--) {
            UndoLog log = logs.get(i);
            if (!log.getTransactionId().equals(this.transactionId)) {
                continue;
            }
            switch (log.getOperationType()) {
                case "INSERT":
                    Database.putData(log.getKey(), log.getOldValue());
                    break;
                case "UPDATE":
                    Database.putData(log.getKey(), log.getOldValue());
                    break;
                case "DELETE":
                    Database.deleteData(log.getKey());
                    break;
            }
        }
    }

    /**
     * 事务提交，代表事务的结束
     *
     * @throws Exception
     */
    public void commit() throws Exception {
        if (!isActive) {
            throw new Exception("事务已提交或回滚");
        }

        // 模拟提交操作
        System.out.println("事务提交: " + transactionId);
        isActive = false;
    }
}

