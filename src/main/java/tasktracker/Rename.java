package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

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
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java rename <old task name> <new task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByRename = logLine.split(" ");
        String oldName = tokenizedByRename[2];
        String newName = tokenizedByRename[3];
        checkNewTaskNameIsAvailable(newName,tasks);
        Task taskToRename = getTaskByName(oldName, tasks);
        taskToRename.rename(newName);
    }

    private void checkNewTaskNameIsAvailable(
                    String newName, ArrayList<Task> tasks) {
        for(Task t: tasks) {
            if(t.getName().equals(newName)) {
                System.out.printf("Error: %s is already a task. "+
                "Please choose another name", newName);
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String oldName = args[1];
        String newName = args[2];
        checkNewTaskNameIsAvailable(newName,tasks);
        Task taskToRename = getTaskByName(oldName, tasks);
        taskToRename.rename(newName);
    }
}
