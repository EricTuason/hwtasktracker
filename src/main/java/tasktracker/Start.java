package tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;

public class Start implements Command{

    @Override
    public void run(String[] args) throws IOException {
        System.out.println(args.length);
        if (args.length > 2) {
            System.out.println("Usage: java TM.java start <task name>");
            throw new InputMismatchException("Too many arguments");
        }
        FileWriter logWriter = getLogWriter();
        String time = getTime();
        String taskToLog = "start %" ;
        logWriter.write(taskToLog);
    }
    
}
