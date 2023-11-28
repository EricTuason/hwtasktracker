package tasktracker;

public class Parser {
    public static Command getCommand(String commandToCheck) {

        Command command = new Start();
        return command;
    }
}
