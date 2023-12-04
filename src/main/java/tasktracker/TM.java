package tasktracker;

import java.nio.file.Files;
import java.nio.file.Paths;
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
            System.out.println(e); //TODO remove this
        }
        
    }

    public static ArrayList<Task> createTasksFromLog() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try (Stream<String> stream = 
                            Files.lines(Paths.get("tasktracker.log"))) {
            stream.forEach(s -> TM.performLogCommand(s,tasks)); //TODO do check for error/comment in log?
        } catch (Exception e) {
            // TODO: handle exception
        }
        return tasks;
    }

    private static void performLogCommand(String s, ArrayList<Task> tasks) {
        Command logCommand = Parser.getCommandFromLog(s);
        logCommand.alterTasks(s, tasks);
    }
}
