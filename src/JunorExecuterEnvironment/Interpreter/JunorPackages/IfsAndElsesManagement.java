package JunorExecuterEnvironment.Interpreter.JunorPackages;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JunorExecuterEnvironment.Interpreter.JunorCommands;

public class IfsAndElsesManagement {

    private static final Pattern varPattern = Pattern.compile("\\$@([a-zA-Z_0-9]+)");

    public static boolean evaluateIf(Matcher ifsElsesGroup, Map<String, Integer> intVars, Map<String, String> strVars) {
        String bruteLeftSide = ifsElsesGroup.group(1);
        String operator = ifsElsesGroup.group(2);
        String bruteRightSide = ifsElsesGroup.group(3);
        String commandThen = ifsElsesGroup.group(4);
        String commandBlockRest = ifsElsesGroup.group(5).trim();

        int val1 = resolveValue(bruteLeftSide, intVars);
        int val2 = resolveValue(bruteRightSide, intVars);

        boolean passInIf = calcComparative(val1, operator, val2);

        if (passInIf) {
            executeCommandBlock(commandThen);
            return true;
        }

        Pattern elifPattern = Pattern.compile("elif\\s?<check=\\{([^\\s}]+)\\s?([><=!]+|=)\\s?([^\\s}]+)\\},\\s?then=\\{([^}]+)\\}>");
        Matcher elifMatcher = elifPattern.matcher(commandBlockRest);

        while (elifMatcher.find()) {
            int eVal1 = resolveValue(elifMatcher.group(1), intVars);
            String eOperator = elifMatcher.group(2);
            int eVal2 = resolveValue(elifMatcher.group(3), intVars);
            String eCommandThen = elifMatcher.group(4);

            if (calcComparative(eVal1, eOperator, eVal2)) {
                executeCommandBlock(eCommandThen);
                return true;
            }
        }

        Pattern elsePattern = Pattern.compile("else=<([^}]+)>");
        Matcher elseMatcher = elsePattern.matcher(commandBlockRest);

        if (elseMatcher.find()) {
            String comandoElse = elseMatcher.group(1);
            executeCommandBlock(comandoElse); // MUDANÇA AQUI
        }

        return false;
    }

    private static void executeCommandBlock(String bruteBlock) {
        String[] commands = bruteBlock.split("\\|");
        for (String cmd : commands) {
            JunorCommands.Interpret(cmd.trim());
        }
    }

    private static boolean calcComparative(int val1, String operator, int val2) {
        switch (operator) {
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

    private static int resolveValue(String token, Map<String, Integer> intVars) {
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
}
