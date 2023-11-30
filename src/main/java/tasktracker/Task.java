package tasktracker;

import java.sql.Time;
import java.util.List;

public class Task {
    private String name;
    private Size size;
    private List<String> description;
    private List<Time> runTimes;
    private Boolean isRunning;
    private Time mostRecentStartTime;

    public void start(Time t) {
        mostRecentStartTime = t;
    }
}
