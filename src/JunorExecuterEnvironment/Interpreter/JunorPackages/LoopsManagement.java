package JunorExecuterEnvironment.Interpreter.JunorPackages;

import JunorExecuterEnvironment.Interpreter.JunorCommands;
import java.util.Map;
import java.util.regex.Matcher;

public class LoopsManagement {
    public static void executeFor(Matcher forMatcher, Map<String, Integer> intVars) {
        String nameVar = forMatcher.group(1);

        String bruteStart = forMatcher.group(2).trim();
        String bruteEnd = forMatcher.group(3).trim();
        String commandBlocks = forMatcher.group(4);

        int start = resolveCalc(bruteStart, intVars);
        int end = resolveCalc(bruteEnd, intVars);

        if (start <= end) {
            for (int i = start; i <= end; i++) {
                intVars.put(nameVar, i);

                String[] commands = commandBlocks.split("\\|");
                for (String cmd : commands) {
                    JunorCommands.Interpret(cmd.trim());
                }
            }
        }
        else {
            for (int i = start; i >= end; i--) {
                intVars.put(nameVar, i);

                String[] commands = commandBlocks.split("\\|");
                for (String cmd : commands) {
                    JunorCommands.Interpret(cmd.trim());
                }
            }
        }
    }

    private static int resolveCalc(String token, Map<String, Integer> intVars) {
        String cleanToken = token.trim();

        if (cleanToken.startsWith("$@")) {
            String nameVar = cleanToken.substring(2);
            return intVars.getOrDefault(nameVar, 0);
        }

        try {
            return Integer.parseInt(cleanToken);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void executeWhile(Matcher whileMatcher, Map<String, Integer> intVars) {
        String bruteLeftSide = whileMatcher.group(1);
        String operator = whileMatcher.group(2);
        String bruteRightSide = whileMatcher.group(3);
        String commandBlock = whileMatcher.group(4);


        while (checkCondition(bruteLeftSide, operator, bruteRightSide, intVars)) {

            String[] commands = commandBlock.split("\\|");
            for (String cmd : commands) {
                JunorCommands.Interpret(cmd.trim());
            }

            try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }//
    }

    private static boolean checkCondition(String esq, String op, String dir, Map<String, Integer> intVars) {
        int val1 = resolveCalc(esq, intVars);
        int val2 = resolveCalc(dir, intVars);

        switch (op) {
            case ">":  return val1 > val2;
            case "<":  return val1 < val2;
            case ">=": return val1 >= val2;
            case "<=": return val1 <= val2;
            case "==": return val1 == val2;
            case "=":  return val1 == val2;
            case "!=": return val1 != val2;
            default: return false;
        }
    }
}
