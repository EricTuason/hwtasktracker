package tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;



public interface Command {

    void writeCommandToLog(String[] args) throws IOException;
    void checkForErrors(String[] args);

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
