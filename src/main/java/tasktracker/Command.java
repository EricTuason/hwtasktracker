package tasktracker;

import java.io.FileWriter;
import java.io.IOException;

public interface Command {

    void run(String[] args) throws IOException;
    
    default FileWriter getLogWriter() throws IOException{
        FileWriter log = new FileWriter("tasktracker.log");
        return log;
    }
}
