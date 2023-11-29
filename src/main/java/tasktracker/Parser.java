package tasktracker;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private static ArrayList<String> possibleCommands = 
    new ArrayList<String>(Arrays.asList(
        "start",
        "stop",
        "size"
        ));

    public static Command getCommand(String[] commandToCheck) 
                        throws IllegalArgumentException {
        checkInputForNull(commandToCheck);
        checkForValidCommand(commandToCheck[0]);
        
        Command command = new Start();
        return command;
    }

    private static void checkForValidCommand(String command) {
        if(!possibleCommands.contains(command)) {
            System.out.println("Illegal Command: Possible Commands:");
            System.out.println(String.join(", ",possibleCommands));
            throw new IllegalArgumentException();
        }
    }

    private static void checkInputForNull(String[] args) 
                        throws IllegalArgumentException {
        if(args.length == 0) {
            System.out.println("Usage: java TM.java <command> <data>");
            throw new IllegalArgumentException();
        }
    }
}
