package tasktracker;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class Summary implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        return;
    }

    @Override
    public void checkForParseErrors(String[] args) {
        if(args.length > 2) {
            System.out.println("Error: Incorrect number of arguments.");
            System.out.println("Usage: summary [<task name> | {S|M|L|XL}]");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void alterTasks(String logLine, ArrayList<Task> tasks) {
        return;
    }

    @Override
    public void performCommand(String[] args, ArrayList<Task> tasks) { //TODO write that we assume size then name
        if(args.length == 2) { 
            String specifier = args[1];
            tasks = filterBySizeOrName(specifier, tasks);
            if(tasks.size() >= 2) {
                printSummaryStatistics(tasks);
            }
        }
        tasks.stream().forEach(Summary::printTask);
    }

    private void printSummaryStatistics(ArrayList<Task> tasks) {
        System.out.println("--- Start of Summary ---");
        System.out.printf("Summary of %s tasks:\n", 
                                    tasks.get(0).getSizeString());
        printMinTaskTime(tasks);
        printMaxTaskTime(tasks);
        printAvgTaskTime(tasks);
        System.out.println("--- End of Summary ---");
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
        Task minTask = tasks.get(0);
        for(Task t: tasks) {
            if(t.getTotalTime().compareTo(minTime) < 0) {
                minTime = t.getTotalTime();
                minTask = t;
            }
        }
        System.out.printf("Minimum Time: %s from task: %s\n", 
                                formatDuration(minTime), minTask.getName());
    }

    private void printMaxTaskTime(ArrayList<Task> tasks) {
        Duration maxTime = tasks.get(0).getTotalTime();
        Task maxTask = tasks.get(0);
        for(Task t: tasks) {
            if(t.getTotalTime().compareTo(maxTime) > 0) {
                maxTime = t.getTotalTime();
                maxTask = t;
            }
        }
        System.out.printf("Maximum Time: %s from task: %s\n", 
                                formatDuration(maxTime), maxTask.getName());
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
        System.out.printf("Task       : %s\n", t.getName());
        System.out.printf("Size       : %s\n", t.getSizeString());
        System.out.printf("Description: %s\n", t.getDescriptionString());
        System.out.printf("Total Time : %s\n", 
                                        formatDuration(t.getTotalTime()));
    }

    private static String formatDuration(java.time.Duration duration) {
        Long s = duration.getSeconds();
        String formatted = String.format("%d:%02d:%02d",
                                 s / 3600, (s % 3600) / 60, (s % 60));
        return formatted;
    }
}
