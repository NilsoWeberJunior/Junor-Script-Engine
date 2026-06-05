package JunorExecuterEnvironment;

import java.util.Scanner;

import JunorExecuterEnvironment.Interpreter.ANSIColors;
import JunorExecuterEnvironment.Interpreter.JunorCommands;

public class JEE {
    public static void main(String[] args) {

        if (args.length > 0) {
            String archiveLocation = args[0];
            try {
                java.io.File archiveToOpen = new java.io.File(archiveLocation);
                if (archiveToOpen.exists()) {
                    JFEE.OpenArchive(archiveToOpen.toURI());
                    System.out.print("press enter to finish process");
                    Scanner scanner = new Scanner(System.in);
                    scanner.nextLine();
                    scanner.close();
                    return;
                } else {
                    return;
                }
            } catch (Exception e) {
            }
        } else {

            Scanner scanner = new Scanner(System.in);

            System.out.println("=========================================================");
            System.out.println("Junor terminal Started");
            System.out.println("Type \"exit\" to terminate the process or type \"help\" for commands.\n");

//
            while (true) {
                System.out.print(ANSIColors.ANSI_BLUE + ">> " + ANSIColors.ANSI_RESET);
                String command = scanner.nextLine().trim();

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println(ANSIColors.ANSI_GREEN + "\nExiting..." + ANSIColors.ANSI_RESET);
                    System.out.println("=========================================================");
                    break;
                }

                if (!command.isEmpty()) {
                    JunorCommands.Interpret(command);
                }
            }
            scanner.close();
        }
    }
}
