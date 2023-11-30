package tasktracker;

import java.io.IOException;

public class Rename implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String oldTaskName = args[1];
        String newTaskName = args[2];
        String time = getTimeString();
        String taskToLog = String.format("%s rename %s %s\n", 
                                        time, oldTaskName, newTaskName);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        checkTaskExists();
    }
    
   private void checkTaskExists() {
        return; //TODO implement after doing task stuff
    }

 private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java rename <old task name> <new task name>");
            throw new IllegalArgumentException();
        }
    }
}
