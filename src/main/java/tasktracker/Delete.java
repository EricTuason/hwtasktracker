package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

public class Delete implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s delete %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
    }
    
    private void checkForValidNumberOfArguments(String[] args){
        if(args.length != 2) {
            System.out.println("Usage: java TM.java delete <task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByDelete = logLine.split(" ");
        String name = tokenizedByDelete[2];
        Task taskToDelete = getTaskByName(name, tasks);
        tasks.remove(taskToDelete);
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        Task taskToDelete = getTaskByName(name, tasks);
        tasks.remove(taskToDelete);
    }
}
