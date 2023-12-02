package tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface Command {

    void writeCommandToLog(String[] args) throws IOException;
    void checkForErrors(String[] args);
    void alterTasks(String s, ArrayList<Task> tasks);

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
}
