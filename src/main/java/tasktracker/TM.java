package tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import java.time.Duration;

public class TM 
{
    public static void main( String[] args )
    {
        try {
            Command command = Parser.getCommand(args);
            command.checkForArgumentErrors(args);
            ArrayList<Task> tasks = createTasksFromLog();
            command.performCommand(args,tasks);
            command.writeCommandToLog(args);
        } catch (Exception e) {
            System.out.println("Terminating program execution");
        }
        
    }

    public static ArrayList<Task> createTasksFromLog() throws Exception {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try (Stream<String> stream = 
                            Files.lines(Paths.get("tasktracker.log"))) {
            stream.forEachOrdered(s -> TM.performLogCommand(s,tasks)); 
        } catch (NoSuchFileException e) {
            return new ArrayList<Task>();
        } catch (Exception e) {
            System.out.println("Error encountered in log file");
            throw e;
        }
        return tasks;
    }

    private static void performLogCommand(String s, ArrayList<Task> tasks) {
        if(isNotATimestamp(s.split(" ")[0])) {
            System.out.println("Error: Noncommand line found in log file");
            throw new IllegalArgumentException();
        }
        Command logCommand = Parser.getCommandFromLog(s);
        logCommand.performLogCommand(s, tasks);
    }

    private static boolean isNotATimestamp(String string) {
        try {
            LocalDateTime.parse(string);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}

class Parser {

    public static Command getCommand(String[] commandToCheck) 
                        throws IllegalArgumentException {
        checkInputForNull(commandToCheck);
        Command command = getCommandInstance(commandToCheck[0]);
        return command;
    }

    private static Command getCommandInstance(String commandName) {
        if(commandName.equals("start")) {return new Start();}
        if(commandName.equals("stop")) {return new Stop();}
        if(commandName.equals("size")) {return new Size();}
        if(commandName.equals("rename")) {return new Rename();}
        if(commandName.equals("delete")) {return new Delete();}
        if(commandName.equals("describe")) {return new Describe();}
        if(commandName.equals("summary")) {return new Summary();}
        System.out.printf("Error: Illegal Command: %s\n", commandName);
        throw new IllegalArgumentException();
    }

    private static void checkInputForNull(String[] args) 
                        throws IllegalArgumentException {
        if(args.length == 0) {
            System.out.println("Usage: java TM.java <command> <data>");
            throw new IllegalArgumentException();
        }
    }

    public static Command getCommandFromLog(String s) {
        String[] tokenized = s.split(" ");
        String commandName = tokenized[1];
        return getCommandInstance(commandName);
    }
}

enum SizeEnum {
    S, M, L, XL;

    public static ArrayList<String> nameString() {
        ArrayList<String> nameArray = new ArrayList<String>();
        for(SizeEnum s : SizeEnum.values()) {
            nameArray.add(s.toString());
        }
        return nameArray;
    }
}

abstract class Command {

    public abstract void checkForArgumentErrors(String[] args);
    public abstract void performLogCommand(String logLine, 
                                           ArrayList<Task> tasks);
    public abstract void performCommand(String[] args, ArrayList<Task> tasks);
    public abstract void writeCommandToLog(String[] args) throws IOException;
    
    protected final void writeToLog(String stringToAddToLog)
                                                         throws IOException{
        FileWriter fw = new FileWriter("tasktracker.log", true);
        try {
            fw.write(stringToAddToLog);
        } catch (Exception e) {
            throw new IOException("Failed to open tasktracker.log");
        } finally {
            fw.close();
        }
    }

    protected final String getTimeString() {
        LocalDateTime time = LocalDateTime.now();
        return time.toString();
    }

    protected final Task getTaskByName(String name, ArrayList<Task> tasks) {
        for(Task task: tasks) {
            if(task.getName().equals(name)) {
                return task;
            }
        }
        System.out.printf("Error: %s not found in task list\n", name);
        throw new IllegalArgumentException();
    }

    protected final Boolean doesTaskExist(String name, ArrayList<Task> tasks) {
        for(Task task: tasks) {
            if(task.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    protected final void createTaskIfNonexistent(
                                                String name,
                                                ArrayList<Task> tasks) {
        if(!doesTaskExist(name, tasks)) {
            if(SizeEnum.nameString().contains(name)) 
            {
                System.out.println("Error: task name cannot be a Size");
                throw new IllegalArgumentException();
            }
            tasks.add(new Task(name));
        }
    }
}

class Delete extends Command{
    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s delete %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForArgumentErrors(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java delete <task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
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

class Describe extends Command{
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
    public void checkForArgumentErrors(String[] args) {
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
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
        String[] splitByQuotes = logLine.split("\"");
        String description = splitByQuotes[1];
        String[] tokenizedInput = splitByQuotes[0].split(" ");
        String name = tokenizedInput[2];
        createTaskIfNonexistent(name, tasks);
        Task taskToDescribe = getTaskByName(name, tasks);
        taskToDescribe.addToDescription(description);
        writeSizeIfPresent(tokenizedInput, taskToDescribe);
    }

    private void writeSizeIfPresent(String[] tokenizedInput, Task task) {
        if(tokenizedInput.length == 4) {
            String size = tokenizedInput[3];
            checkForAllowedSize(size);
            task.setSize(SizeEnum.valueOf(size));
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

class Rename extends Command{

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
    public void checkForArgumentErrors(String[] args) {
        if(args.length != 3) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java rename <old task name> <new task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
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
                "Please choose another name\n", newName);
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

class Size extends Command{

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
    public void checkForArgumentErrors(String[] args) {
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
                "Usage: java TM.java size <task name> {S|M|L|XL}\n",
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
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
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

class Start extends Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s start %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForArgumentErrors(String[] args) {
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
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
        String[] tokenizedByStart = logLine.split(" ");
        LocalDateTime startTime = LocalDateTime.parse(tokenizedByStart[0]);
        String name = tokenizedByStart[2];
        ensureNoTasksAreRunning(tasks);
        startTask(name, startTime, tasks);
    }

    private void startTask(
            String name, LocalDateTime startTime, ArrayList<Task> tasks) {
        createTaskIfNonexistent(name, tasks);
        Task task = getTaskByName(name, tasks);
        task.start(startTime);
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        ensureNoTasksAreRunning(tasks);
        LocalDateTime startTime = LocalDateTime.now();
        startTask(name,startTime,tasks);
    }
}

class Stop extends Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException{
        String taskname = args[1];
        String time = getTimeString();
        String taskToLog = String.format("%s stop %s\n", time, taskname);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForArgumentErrors(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java TM.java stop <task name>");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
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
    
    private void ensureTaskExistsAndWasStarted(
                                String name, ArrayList<Task> tasks) {
        Task task = getTaskByName(name, tasks);
        if(!task.isRunning()) {
            System.out.printf("Error: task %s is not running\n",name);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        String name = args[1];
        ensureTaskExistsAndWasStarted(name, tasks);
        LocalDateTime stopTime = LocalDateTime.now();
        stopTask(name,stopTime,tasks);
    }
}

class Summary extends Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        return;
    }

    @Override
    public void checkForArgumentErrors(String[] args) {
        if(args.length > 2) {
            System.out.println("Error: Incorrect number of arguments.");
            System.out.println("Usage: summary [<task name> | {S|M|L|XL}]");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void performLogCommand(String logLine, ArrayList<Task> tasks) {
        return;
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) {
        if(args.length == 2) { 
            String specifier = args[1];
            tasks = filterBySizeOrName(specifier, tasks);
            if(tasks.size() >= 2) {
                printSummaryStatistics(tasks);
            }
        }
        printSummary(tasks);
    }

    private void printSummary(ArrayList<Task> tasks) {
        if(tasks.size() == 0) {
            System.out.println("No tasks to print.");
            return;
        }
        System.out.println("--- Start of Summary ---");
        tasks.stream().forEachOrdered(Summary::printTask);
        System.out.println("---- End of Summary ----");
    }

    private void printSummaryStatistics(ArrayList<Task> tasks) {
        System.out.println("--- Start of Statistics ---");
        System.out.printf("Summary of %s tasks:\n", 
                                    tasks.get(0).getSizeString());
        printMinTaskTime(tasks);
        printMaxTaskTime(tasks);
        printAvgTaskTime(tasks);
        System.out.println("---- End of Statistics ----");
    }

    private void printAvgTaskTime(ArrayList<Task> tasks) {
        Duration total = Duration.ofSeconds(0);
        for(Task t : tasks) {
            total = total.plus(t.getTotalTime());
        }
        System.out.printf("Average Time: %s\n",
                    formatDuration(total.dividedBy(tasks.size())));
    }

    private void printMinTaskTime(ArrayList<Task> tasks) {
        Duration minTime = tasks.get(0).getTotalTime();
        for(Task t: tasks) {
            if(t.getTotalTime().compareTo(minTime) < 0) {
                minTime = t.getTotalTime();
            }
        }
        System.out.printf("Minimum Time: %s\n", formatDuration(minTime));
    }

    private void printMaxTaskTime(ArrayList<Task> tasks) {
        Duration maxTime = tasks.get(0).getTotalTime();
        for(Task t: tasks) {
            if(t.getTotalTime().compareTo(maxTime) > 0) {
                maxTime = t.getTotalTime();
            }
        }
        System.out.printf("Maximum Time: %s\n", formatDuration(maxTime));
    }

    private Boolean isSizeParameter(String s) {
        ArrayList<String> accepetedSizes = SizeEnum.nameString();
        if(accepetedSizes.contains(s)) { return true; }
        return false;
    }

    private ArrayList<Task> sizedTasks(String size, ArrayList<Task> tasks) {
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        for (Task t: tasks) {
            if(t.getSizeString().equals(size)) {
                filteredTasks.add(t);
            }
        }
        return filteredTasks;
    }

    private ArrayList<Task> filterBySizeOrName(
                            String specifier, ArrayList<Task> tasks) {
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        if(!isSizeParameter(specifier)) {
            filteredTasks.add(getTaskByName(specifier, tasks));
        } else {
            filteredTasks = sizedTasks(specifier, tasks);
        }
        return filteredTasks;
    }

    private static void printTask(Task t) {
        System.out.printf("Task Name   : %s\n", t.getName());
        System.out.printf("Size        : %s\n", t.getSizeString());
        System.out.printf("Description : %s\n", t.getDescriptionString());
        System.out.printf("Total Time  : %s\n", 
                                        formatDuration(t.getTotalTime()));
    }

    private static String formatDuration(java.time.Duration duration) {
        Long s = duration.getSeconds();
        String formatted = String.format("%d:%02d:%02d",
                                 s / 3600, (s % 3600) / 60, (s % 60));
        return formatted;
    }
}

class Task {
    private String name;
    private Optional<SizeEnum> size; 
    private ArrayList<String> description;
    private ArrayList<Duration> runTimes;
    private Optional<LocalDateTime> currentStartTime;

    public Task(String name) {
        this.name = name;
        this.description = new ArrayList<String>();
        this.runTimes = new ArrayList<Duration>();
        this.currentStartTime = Optional.empty();
        this.size = Optional.empty();
    }

    public String getName() {
        return name;
    }

    public void start(LocalDateTime t) {
        currentStartTime = Optional.of(t);
    }

    public void stop(LocalDateTime t) {
        Duration runTime = Duration.between(currentStartTime.get(), t);
        if(runTime.isNegative()) {
            System.out.printf(
                "Error: End time %s is before start time %s for task %s\n", 
                currentStartTime.toString() , t.toString(), name);
        }
        runTimes.add(runTime);
        currentStartTime = Optional.empty();
    }

    public void setSize(SizeEnum size) {
        this.size = Optional.of(size);
    }

    public String getSizeString() {
        if(!size.isPresent()) {
            return "";
        }
        return size.get().toString();
    }

    public void addToDescription(String sentence) {
        description.add(sentence);
    }

    public String getDescriptionString() {
        return String.join("\n", description);
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public Duration getTotalTime() {
        if(runTimes.size()==0) {return Duration.ZERO;}
        Duration total = Duration.ofSeconds(0);
        for(Duration d : runTimes) {
            total = total.plus(d);
        }
        return total;
    }

    public Boolean isRunning() {
        if(currentStartTime.isPresent()) {
            return true;
        }
        return false;
    }
}
