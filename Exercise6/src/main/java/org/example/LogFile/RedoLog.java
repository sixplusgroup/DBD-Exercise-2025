package org.example.LogFile;

public class RedoLog {
    //该记录对应的事务ID
    private String transactionId;

    //操作类型
    private String operationType;

    //操作的键值
    private String key;

    //操作后的值
    private String value;

    public RedoLog(String transactionId, String operationType, String key, String value) {
        this.transactionId = transactionId;
        this.operationType = operationType;
        this.key = key;
        this.value = value;
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

    public String getValue() {
        return value;
    }
}

