package org.example;


import org.example.LogFile.UndoLogFile;
import org.example.LogFile.RedoLogFile;
import org.example.LogFile.UndoLog;
import org.example.LogFile.RedoLog;

import java.io.IOException;
import java.util.List;

public class WAL {
    private RedoLogFile redoLogFile;
    private UndoLogFile undoLogFile;

    public WAL() {
        this.redoLogFile = new RedoLogFile();
        this.undoLogFile = new UndoLogFile();
    }

    //记录 Redo 日志
    public void logRedo(String transactionId, String operationType, String key, String value) {
        redoLogFile.addLog(new RedoLog(transactionId, operationType, key, value));
    }

    //记录 Undo 日志
    public void logUndo(String transactionId, String operationType, String key, String oldValue) {
        undoLogFile.addLog(new UndoLog(transactionId, operationType, key, oldValue));
    }

    //获取UndoLog用于回滚等操作
    public List<UndoLog> getUndoLogs() {
        return undoLogFile.getLogs();
    }

    /**
     * TODO
     * 数据库崩溃之后的恢复
     * 你需要利用Redo日志文件中的记录恢复Database中的数据
     */
    public void recover() {
        List<RedoLog> logs = redoLogFile.getLogs();
        for (RedoLog log : logs) {
            switch (log.getOperationType()) {
                case "INSERT":
                    Database.putData(log.getKey(), log.getValue());
                    break;
                case "UPDATE":
                    Database.putData(log.getKey(), log.getValue());
                    break;
                case "DELETE":
                    Database.deleteData(log.getKey());
                    break;
            }
        }
    }
}
