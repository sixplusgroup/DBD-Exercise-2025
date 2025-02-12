package com.nju.edu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockManager {
    private Map<String, LockInfo> locks = new ConcurrentHashMap<>();

    public void acquireLock(String resourceId, int transactionId) {
        LockInfo lockInfo = locks.computeIfAbsent(resourceId, k -> new LockInfo());
        lockInfo.acquire(transactionId);
    }

    public void releaseLock(String resourceId, int transactionId) {
        LockInfo lockInfo = locks.get(resourceId);
        if (lockInfo != null) {
            lockInfo.release(transactionId);
        }
    }

    private static class LockInfo {
        private int lockOwner = -1;

        public void acquire(int transactionId) {
            if (lockOwner == -1 || lockOwner == transactionId) {
                lockOwner = transactionId;
            } else {
                System.out.println("Resource is locked by another transaction");
            }
        }

        public void release(int transactionId) {
            if (lockOwner == transactionId) {
                lockOwner = -1;
            }
        }
    }
}

