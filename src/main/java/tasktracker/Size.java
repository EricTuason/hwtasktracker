package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

public class Size implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String size = args[2];
        String time = getTimeString();
        String taskToLog = String.format("%s size %s %s\n", 
                                        time, taskname, size);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        String size = args[2];
        checkForAllowedSize(size);
    }

    private void checkForAllowedSize(String size) {
        ArrayList<String> accepetedSizes = SizeEnum.nameString();
        if(!accepetedSizes.contains(size))
        {
            System.out.printf(
                "Error: illegal size %s\n" +
                "Usage: java TM.java size <task name> {S|M|L|XL}",
                size);
            throw new IllegalArgumentException();
        }
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java size <task name> {S|M|L|XL}");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByStart = logLine.split(" ");
        String name = tokenizedByStart[2];
        String size = tokenizedByStart[3];
        sizeTask(name, size, tasks);
    }

    private void sizeTask(String name, String size, ArrayList<Task> tasks) {
        checkForAllowedSize(size);
        createTaskIfNonexistent(name, tasks);
        Task task = getTaskByName(name,tasks);
        task.setSize(SizeEnum.valueOf(size));
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        String size = args[2];
        sizeTask(name, size, tasks);
    }
}
