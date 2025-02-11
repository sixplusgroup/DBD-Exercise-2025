package org.example.LogFile;

public class UndoLog {
    //该记录对应的事务ID
    private String transactionId;

    //操作类型
    private String operationType;

    //操作的键值
    private String key;

    //操作发生前的键对应的原值
    private String oldValue;

    public UndoLog(String transactionId, String operationType, String key, String oldValue) {
        this.transactionId = transactionId;
        this.operationType = operationType;
        this.key = key;
        this.oldValue = oldValue;
    }

    // Getter methods
    public String getTransactionId() {
        return transactionId;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getKey() {
        return key;
    }

    public String getOldValue() {
        return oldValue;
    }
}
