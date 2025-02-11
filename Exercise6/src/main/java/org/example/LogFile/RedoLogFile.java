package org.example.LogFile;

import java.util.ArrayList;
import java.util.List;


public class RedoLogFile {

    private List<RedoLog> logs;

    public RedoLogFile() {
        logs = new ArrayList<>();
        addInitial();
    }

    public void addInitial(){
        logs.add(new RedoLog("T99","INSERT","k99","0"));
        logs.add(new RedoLog("T100","UPDATE","k99","1"));
        logs.add(new RedoLog("T100","INSERT","k100","1"));
        logs.add(new RedoLog("T100","DELETE","k100","null"));
    }

    public List<RedoLog> getLogs(){
        return new ArrayList<>(logs);
    }

    public void addLog(RedoLog redoLog){
        logs.add(redoLog);
    }
}
