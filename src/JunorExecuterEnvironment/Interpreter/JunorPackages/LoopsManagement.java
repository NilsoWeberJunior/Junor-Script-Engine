package JunorExecuterEnvironment.Interpreter.JunorPackages;

import JunorExecuterEnvironment.Interpreter.JunorCommands;
import java.util.Map;
import java.util.regex.Matcher;

public class LoopsManagement {

    public static void executeFor(Matcher forMatcher, Map<String, Integer> intVars, String loopBlock) {
        String nameVar = forMatcher.group(1);

        String bruteStart = forMatcher.group(2).trim();
        String bruteEnd = forMatcher.group(3).trim();

        int start = resolveCalc(bruteStart, intVars);
        int end = resolveCalc(bruteEnd, intVars);

        if (start <= end) {
            for (int i = start; i <= end; i++) {
                intVars.put(nameVar, i);
                executeCommandBlockSeguro(loopBlock);
                try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            }
        }
        else {
            for (int i = start; i >= end; i--) {
                intVars.put(nameVar, i);
                executeCommandBlockSeguro(loopBlock);
                try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
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

    public static void executeWhile(Matcher whileMatcher, Map<String, Integer> intVars, String blocoLoop) {
        String bruteLeftSide = whileMatcher.group(1);
        String operator = whileMatcher.group(2);
        String bruteRightSide = whileMatcher.group(3);

        while (checkCondition(bruteLeftSide, operator, bruteRightSide, intVars)) {
            executeCommandBlockSeguro(blocoLoop);

            try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    private static void executeCommandBlockSeguro(String bruteBlock) {
        int nivelChaves = 0;
        StringBuilder comandoAtual = new StringBuilder();

        for (int i = 0; i < bruteBlock.length(); i++) {
            char c = bruteBlock.charAt(i);

            if (c == '{') nivelChaves++;
            else if (c == '}') nivelChaves--;

            if (c == '|' && nivelChaves == 0) {
                JunorCommands.Interpret(comandoAtual.toString().trim());
                comandoAtual.setLength(0);
            } else {
                comandoAtual.append(c);
            }
        }
        if (comandoAtual.length() > 0) {
            JunorCommands.Interpret(comandoAtual.toString().trim());
        }
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
