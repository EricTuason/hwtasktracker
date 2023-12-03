package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

public class Describe implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String description = args[2];
        if(args.length == 3) {
            writeDescriptionWithoutSizeToLog(taskname,description);
        } else if (args.length == 4) {
            String size = args[3];
            writeDescriptionWithSizeToLog(taskname,description,size);
        }
    }

    private void writeDescriptionWithSizeToLog(String taskname, 
                                               String description, 
                                               String size) 
                                               throws IOException {
        String time = getTimeString();
        String taskToLog = String.format(
            "%s describe %s %s \"%s\"\n", 
            time, taskname, size, description);
        writeToLog(taskToLog);
    }

    private void writeDescriptionWithoutSizeToLog(String taskname,
                                                  String description) 
                                                  throws IOException {
        String time = getTimeString();
        String taskToLog = String.format(
            "%s describe %s \"%s\"\n", 
            time, taskname, description);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForParseErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        if(args.length == 4) {
            String size = args[3];
            checkForAllowedSize(size);
        }
    }

    private void checkForAllowedSize(String size) {
        ArrayList<String> accepetedSizes = SizeEnum.nameString();
        if(!accepetedSizes.contains(size))
        {
            System.out.println(
                "Error: illegal size\n" +
                "Usage: java TM.java describe " +
                "<task name> <description> [{S|M|L|XL}]");
            throw new IllegalArgumentException();
        }
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3 && args.length != 4) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java describe " +
                "<task name> <description> [{S|M|L|XL}]");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) { //TODO refactor this method 
        String[] t = logLine.split("\"");
        String description = t[1];
        String[] ti = t[0].split(" ");
        String name = ti[2];
        createTaskIfNonexistent(name, tasks);
        Task taskToDescribe = getTaskByName(name, tasks);
        taskToDescribe.addToDescription(description);
        if(ti.length == 4) {
            String size = ti[3];
            checkForAllowedSize(size);
            taskToDescribe.setSize(SizeEnum.valueOf(size));
        }
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        String description = args[2];
        createTaskIfNonexistent(name, tasks);
        Task task = getTaskByName(name, tasks);
        task.addToDescription(description);
        if(args.length==4) {
            String size = args[3];
            task.setSize(SizeEnum.valueOf(size));
        }
    }
}
