package tasktracker;

import java.util.ArrayList;

public class TM 
{
    public static void main( String[] args )
    {
        try {
            Command command = Parser.getCommand(args);
            //TODO run to get objects from log
            ArrayList<Task> tasks = Task.createTasksFromLog();
            command.checkForErrors(args);
            command.writeCommandToLog(args);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
