package tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface Command {

    void writeCommandToLog(String[] args) throws IOException;
    void checkForParseErrors(String[] args);
    void alterTasks(String logLine, ArrayList<Task> tasks);
    void performCommand(String[] args, ArrayList<Task> tasks);

    default void writeToLog(String stringToAddToLog) throws IOException{
        FileWriter fw = new FileWriter("tasktracker.log", true);
        try {
            fw.write(stringToAddToLog);
        } catch (Exception e) {
            throw new IOException("Failed to open tasktracker.log");
        } finally {
            fw.close();
        }
    }

    default String getTimeString() {
        LocalDateTime time = LocalDateTime.now();
        return time.toString();
    }

    default Task getTaskByName(String name, ArrayList<Task> tasks) {
        for(Task task: tasks) {
            if(task.getName().equals(name)) {
                return task;
            }
        }
        System.out.printf("Error: %s not found in task list\n", name);
        throw new IllegalArgumentException();
    }

    default Boolean doesTaskExist(String name, ArrayList<Task> tasks) {
        for(Task task: tasks) {
            if(task.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
