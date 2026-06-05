package JunorExecuterEnvironment.Interpreter.JunorPackages;

import JunorExecuterEnvironment.Interpreter.ANSIColors;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntegerManagement {
    public static int finalText = 0;

    private static final Pattern varPattern = Pattern.compile("\\$@([a-zA-Z_0-9]+)");

    public static void calc(Matcher groupCalc, Map<String, Integer> intVars) {
        int num1 = extractValue(groupCalc.group(2).trim(), intVars);
        int num2 = extractValue(groupCalc.group(4).trim(), intVars);
        String operator = groupCalc.group(3).trim();

        if (operator.equals("+")) {
            finalText = num1 + num2;
        }
        else if (operator.equals("-")) {
            finalText = num1 - num2;
        }
        else if (operator.equals("x")) {
            finalText = num1 * num2;
        } else {
            System.out.println(ANSIColors.ANSI_RED + "");
        }

        intVars.put(groupCalc.group(1).trim(), finalText);

        if (groupCalc.group(5).equals("true")) {
            System.out.println(finalText);
        }
    }

    private static int extractValue(String token, Map<String, Integer> intVars) {
        Matcher varMatcher = varPattern.matcher(token);

        if (varMatcher.matches()) {
            String varName = varMatcher.group(1);
            if (intVars.containsKey(varName)) {
                return intVars.get(varName);
            } else {
                System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Variable not found: " + token + ANSIColors.ANSI_RESET);
                return 0;
            }
        }

        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Invalid number: " + token + ANSIColors.ANSI_RESET);
            return 0;
        }
    }
}
