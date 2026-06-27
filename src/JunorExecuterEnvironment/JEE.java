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
            } catch (Exception e) {}
        } else {

            Scanner scanner = new Scanner(System.in);

            System.out.println("=========================================================");
            System.out.println("Junor version 0.0.5");
            System.out.println("Junor terminal Started");
            System.out.println("Powered by: Java OpenJDK");
            System.out.println("Type \"exit\" to terminate the process or type \"help\" for commands.\n");

            StringBuilder blocoMultiLinhas = new StringBuilder();
            int chavesAbertas = 0;

            System.out.print(ANSIColors.ANSI_BLUE + ">> " + ANSIColors.ANSI_RESET);

            while (scanner.hasNextLine()) {
                String entrada = scanner.nextLine();
                String entradaTrimmed = entrada.trim();

                if (entradaTrimmed.equalsIgnoreCase("exit")) {
                    System.out.println(ANSIColors.ANSI_GREEN + "\nExiting..." + ANSIColors.ANSI_RESET);
                    System.out.println("=========================================================");
                    break;
                }

                for (int i = 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) == '{') chavesAbertas++;
                    if (entrada.charAt(i) == '}') chavesAbertas--;
                }

                blocoMultiLinhas.append(entrada).append("\n");

                if (chavesAbertas == 0) {
                    String comandoCompleto = blocoMultiLinhas.toString().trim();
                    blocoMultiLinhas.setLength(0);

                    if (!comandoCompleto.isEmpty()) {
                        JunorCommands.Interpret(comandoCompleto);
                    }

                    System.out.print(ANSIColors.ANSI_BLUE + ">> " + ANSIColors.ANSI_RESET);
                } else {
                    System.out.print(ANSIColors.ANSI_BLUE + ".. " + ANSIColors.ANSI_RESET);
                }
            }
            scanner.close();
        }
    }
}

