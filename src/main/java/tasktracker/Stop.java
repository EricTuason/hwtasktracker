package tasktracker;

import java.io.IOException;

public class Stop implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s stop %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        ensureTaskExistsAndWasStarted();
    }

    private void ensureTaskExistsAndWasStarted() {
        return; //TODO implement this function after tasks analysis is
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java stop <task name>");
            throw new IllegalArgumentException();
        }
    }
}
