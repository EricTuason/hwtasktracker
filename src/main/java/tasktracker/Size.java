package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

public class Size implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String size = args[2];
        String time = getTimeString();
        String taskToLog = String.format("%s size %s %s\n", 
                                        time, taskname, size);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        checkForAllowedSize(args);
    }

    private void checkForAllowedSize(String[] args) {
        String size = args[2];
        ArrayList<String> accepetedSizes = SizeEnum.nameString();
        if(!accepetedSizes.contains(size))
        {
            System.out.println(
                "Error: illegal size\n" +
                "Usage: java TM.java size <task name> {S|M|L|XL}");
            throw new IllegalArgumentException();
        }
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java size <task name> {S|M|L|XL}");
            throw new IllegalArgumentException();
        }
    }
    
}
