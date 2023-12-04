package tasktracker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TM 
{
    public static void main( String[] args )
    {
        try {
            Command command = Parser.getCommand(args);
            command.checkForParseErrors(args);
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
            stream.forEach(s -> TM.performLogCommand(s,tasks)); 
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
        logCommand.alterTasks(s, tasks);
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
