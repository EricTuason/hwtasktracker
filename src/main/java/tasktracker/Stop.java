package tasktracker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Stop implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s stop %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
    }

    private void ensureTaskExistsAndWasStarted(
                                String name, ArrayList<Task> tasks) {
        Task task = getTaskByName(name, tasks);
        if(!task.isRunning()) {
            System.out.printf("Error: task %s is not running\n",name);
            throw new IllegalArgumentException();
        }
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java stop <task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByStop = logLine.split(" ");
        LocalDateTime stopTime = LocalDateTime.parse(tokenizedByStop[0]);
        String name = tokenizedByStop[2];
        stopTask(name, stopTime, tasks);
    }

    private void stopTask(
        String name, LocalDateTime stopTime, ArrayList<Task> tasks) {
            ensureTaskExistsAndWasStarted(name, tasks);
            Task task = getTaskByName(name, tasks);
            task.stop(stopTime);
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        ensureTaskExistsAndWasStarted(name, tasks);
        LocalDateTime stopTime = LocalDateTime.now();
        stopTask(name,stopTime,tasks);
    }
}
