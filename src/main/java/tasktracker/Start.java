package tasktracker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Start implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s start %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java start <task name>");
            throw new IllegalArgumentException();
        }
    }
    
    private void ensureNoTasksAreRunning(ArrayList<Task> tasks) {
        for(Task t: tasks) {
            if(t.isRunning()) {
                System.out.printf(
                    "Error: task %s is already running. " +
                    "Only one task may run at one time.\n", 
                    t.getName());
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByStart = logLine.split(" ");
        LocalDateTime startTime = LocalDateTime.parse(tokenizedByStart[0]);
        String name = tokenizedByStart[2];
        ensureNoTasksAreRunning(tasks);
        startTask(name, startTime, tasks);
    }

    private void startTask(
            String name, LocalDateTime startTime, ArrayList<Task> tasks) {
        if(doesTaskExist(name, tasks)) {
            Task task = getTaskByName(name, tasks);
            task.start(startTime);
        } else {
            Task task = new Task(name);
            task.start(startTime);
            tasks.add(task);
        }
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        ensureNoTasksAreRunning(tasks);
        LocalDateTime startTime = LocalDateTime.now();
        startTask(name,startTime,tasks);
    }
}
