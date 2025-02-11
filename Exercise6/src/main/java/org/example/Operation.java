package org.example;

public class Operation {
    //操作类型
    private String type;

    //操作对应键值
    private String key;

    //操作修改后的值，DELETE操作对应的value可考虑为null
    private String value;

    public Operation(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    // Getter methods
    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

