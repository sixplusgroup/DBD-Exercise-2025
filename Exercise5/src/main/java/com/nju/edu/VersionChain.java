package com.nju.edu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class VersionChain {

    private final String resourceId = UUID.randomUUID().toString();

    private final List<Data> dataList = new ArrayList<>();

    /**
     * 最新版本号
     */
    private static final AtomicInteger currentVersion = new AtomicInteger(0);

    /**
     * 最新正确版本号
     * 只有当事务提交时才会更新 newestRightVersion
     * 事务未提交时更新数据不改变 newestRightVersion
     */
    private static final AtomicInteger newestRightVersion = new AtomicInteger(0);

    public VersionChain(Data data) {
        dataList.add(data);
        currentVersion.set(data.getVersion());
        newestRightVersion.set(data.getVersion());
    }

    /**
     * 将变量的值更新为 newObject
     * @param newObject
     * @return 本次更新的值对应的版本号
     */
    public int update(Object newObject) {
        // TODO
        return -1;
    }

    /**
     * 从版本链中找到对应版本的 Data
     * @param version 版本号
     * @return 对应版本的 Data
     */
    public Data getDataByVersion(int version) {
        return dataList.stream()
                .filter(data -> data.getVersion() == version)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
    }

    public String getResourceId() {
        return resourceId;
    }

    public int getCurrentVersion() {
        return currentVersion.get();
    }

    public int getNewestRightVersion() {
        return newestRightVersion.get();
    }

    public void updateNewestVersion(int version) {
        newestRightVersion.set(version);
    }
}
