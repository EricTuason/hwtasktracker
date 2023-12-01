package tasktracker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class Task {
// TODO mention only size and startTime needs to be optional since all others are just empty 
    private String name;
    private Optional<SizeEnum> size; 
    private ArrayList<String> description;
    private ArrayList<Duration> runTimes;
    private Optional<LocalDateTime> mostRecentStartTime;

    public Task(String name) {
        this.name = name;
        this.description = new ArrayList<String>();
        this.runTimes = new ArrayList<Duration>();
        this.mostRecentStartTime = Optional.empty();
    }

    public void start(LocalDateTime t) {
        mostRecentStartTime = Optional.of(t);
    }

    public void stop(LocalDateTime t) {
        Duration runTime = Duration.between(mostRecentStartTime.get(), t);
        if(runTime.isNegative()) {
            System.out.printf(
                "Error: End time %s is before start time %s for task %s", 
                mostRecentStartTime.toString() , t.toString(), name);
        }
        runTimes.add(runTime);
        mostRecentStartTime = Optional.empty();
    }

    public void setSize(SizeEnum size) {
        this.size = Optional.of(size);
    }

    public String getSizeString() {
        if(!size.isPresent()) {
            return "Unspecified";
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
        Duration total = Duration.ZERO;
        for(Duration d : runTimes) {
            total.plus(d);
        }
        return total;
    }

    public Duration getMinTime() {
        if(runTimes.size()==0) {return Duration.ZERO;}
        return Collections.min(runTimes);
    }

    public Duration getMaxTime() {
        if(runTimes.size()==0) {return Duration.ZERO;}
        return Collections.min(runTimes);
    }

    public Duration getAvgTime() {
        if(runTimes.size()==0) {return Duration.ZERO;}
        Duration total = getTotalTime();
        Duration average = total.dividedBy(runTimes.size()); 
        return average;
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
