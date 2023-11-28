package tasktracker;

public class TM 
{
    public static void main( String[] args )
    {
        try {
            Command command = Parser.getCommand(args[0]);
            command.run(args);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
