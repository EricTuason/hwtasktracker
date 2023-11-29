package tasktracker;

import java.io.IOException;

public class Start implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        writeStartTimeToLog(taskname);   
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        ensureNoOtherTasksAreRunning();
    }
    
    private void writeStartTimeToLog(String taskname) throws IOException {
        String time = getTimeString();
        String taskToLog = String.format("%s start %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    private void ensureNoOtherTasksAreRunning() {
        return; //TODO implement this function after tasks analysis is
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java start <task name>");
            throw new IllegalArgumentException();
        }
    }

}
