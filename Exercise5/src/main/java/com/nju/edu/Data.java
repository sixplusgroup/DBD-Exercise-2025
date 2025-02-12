package com.nju.edu;

public class Data {

    private Object object;

    private int version = 0;

    public Data(Object object) {
        this.object = object;
    }

    public Data(Object object, int version) {
        this.object = object;
        this.version = version;
    }

    // Getters and Setters
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
