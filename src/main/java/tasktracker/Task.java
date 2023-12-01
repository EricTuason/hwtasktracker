package tasktracker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Task {
    private String name;
    private Size size;
    private ArrayList<String> description;
    private ArrayList<Time> runTimes;
    private Boolean isRunning;
    private Time mostRecentStartTime;

    public void start(Time t) {
        mostRecentStartTime = t;
    }

    public static ArrayList<Task> createTasksFromLog() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try (Stream<String> stream = 
                            Files.lines(Paths.get("tasktracker.log"))) {
            //stream.forEach(Command::); //TODO find appropriate name for this function 
        } catch (Exception e) {
            // TODO: handle exception
        }
        return tasks;
    }
}
