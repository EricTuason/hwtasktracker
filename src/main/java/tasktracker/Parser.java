package tasktracker;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private static ArrayList<String> possibleCommands = 
    new ArrayList<String>(Arrays.asList(
        "start",
        "stop",
        "size",
        "rename",
        "delete",
        "describe"
        ));

    public static Command getCommand(String[] commandToCheck) 
                        throws IllegalArgumentException {
        checkInputForNull(commandToCheck);
        checkForValidCommand(commandToCheck[0]);
        Command command = getCommandInstance(commandToCheck[0]);
        return command;
    }

    private static Command getCommandInstance(String commandName) {
        if(commandName.equals("start")) {return new Start();}
        if(commandName.equals("stop")) {return new Stop();}
        if(commandName.equals("size")) {return new Size();}
        if(commandName.equals("rename")) {return new Rename();}
        if(commandName.equals("delete")) {return new Delete();}
        if(commandName.equals("describe")) {return new Describe();}
        throw new IllegalArgumentException("Uknown command");
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

    public static Command getCommandFromLog(String s) {
        String[] tokenized = s.split(" ");
        String commandName = tokenized[1];
        System.out.println(commandName);
        return getCommandInstance(commandName);
    }
}
