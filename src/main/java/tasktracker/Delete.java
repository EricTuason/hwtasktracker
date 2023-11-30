package tasktracker;

import java.io.IOException;

public class Delete implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s delete %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        ensureTaskExists();
    }
    
    private void checkForValidNumberOfArguments(String[] args){
        if(args.length != 2) {
            System.out.println("Usage: java TM.java delete <task name>");
            throw new IllegalArgumentException();
        }
    }

    private void ensureTaskExists() {
        return; //TODO implent after task
    }
}
