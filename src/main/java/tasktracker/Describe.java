package tasktracker;

import java.io.IOException;
import java.util.ArrayList;

public class Describe implements Command{

    @Override
    public void writeCommandToLog(String[] args) throws IOException {
        String taskname = args[1];
        String description = args[2];
        if(args.length == 3) {
            writeDescriptionWithoutSizeToLog(taskname,description);
        } else if (args.length == 4) {
            String size = args[3];
            writeDescriptionWithSizeToLog(taskname,description,size);
        }
    }

    private void writeDescriptionWithSizeToLog(String taskname, 
                                               String description, 
                                               String size) 
                                               throws IOException {
        String time = getTimeString();
        String taskToLog = String.format(
            "%s describeSize %s %s \"%s\"\n", 
            time, taskname, size, description);
        writeToLog(taskToLog);
    }

    private void writeDescriptionWithoutSizeToLog(String taskname,
                                                  String description) 
                                                  throws IOException {
        String time = getTimeString();
        String taskToLog = String.format(
            "%s describe %s \"%s\"\n", 
            time, taskname, description);
        writeToLog(taskToLog);
    }

    @Override
    public void checkForErrors(String[] args) {
        checkForValidNumberOfArguments(args);
        checkForAppropriateSize(args);
    }

    private void checkForAppropriateSize(String[] args) {
        if(args.length == 4) {
            String size = args[3];
            ArrayList<String> accepetedSizes = SizeEnum.nameString();
            if(!accepetedSizes.contains(size))
            {
                System.out.println(
                    "Error: illegal size\n" +
                    "Usage: java TM.java describe " +
                    "<task name> <description> [{S|M|L|XL}]");
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkForValidNumberOfArguments(String[] args) {
        if(args.length != 3 && args.length != 4) {
            System.out.println(
                "Error: incorrect number of arguments\n" +
                "Usage: java TM.java describe " +
                "<task name> <description> [{S|M|L|XL}]");
            throw new IllegalArgumentException();
        }
    }
}
