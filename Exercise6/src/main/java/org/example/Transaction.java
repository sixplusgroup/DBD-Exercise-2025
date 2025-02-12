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
        //TODO
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

        //TODO

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
        //TODO
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
        //TODO
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
        isActive = false;
    }
}

