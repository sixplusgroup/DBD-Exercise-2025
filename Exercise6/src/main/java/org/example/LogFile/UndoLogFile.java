package org.example.LogFile;

import java.util.ArrayList;
import java.util.List;

public class UndoLogFile {
    private List<UndoLog> logs;

    public UndoLogFile() {
        logs = new ArrayList<>();
    }

    public List<UndoLog> getLogs() {
        return new ArrayList<>(logs);
    }

    public void addLog(UndoLog undoLog) {
        logs.add(undoLog);
    }
}
