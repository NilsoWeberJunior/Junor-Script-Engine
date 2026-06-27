package JunorExecuterEnvironment.Interpreter.JunorPackages;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import JunorExecuterEnvironment.Interpreter.JunorCommands;

public class IfsAndElsesManagement {


    public static boolean evaluateIf(Matcher ifsElsesGroup, Map<String, Integer> intVars, Map<String, String> strVars, Map<String, String> boolVars, String linhaCompleta) {        String bruteLeftSide = ifsElsesGroup.group(1);
        String operator = ifsElsesGroup.group(2);
        String bruteRightSide = ifsElsesGroup.group(3);

        int fimDoThen = ifsElsesGroup.end();
        Object[] resultadoThen = separarBlocoPorContador(linhaCompleta, fimDoThen);
        String commandThen = (String) resultadoThen[0];
        int fimDoBlocoThenNaLinha = (int) resultadoThen[1];

        String commandBlockRest = linhaCompleta.substring(fimDoBlocoThenNaLinha).trim();
        if (commandBlockRest.startsWith(")")) {
            commandBlockRest = commandBlockRest.substring(1).trim();
        }

        String val1 = resolveValue(bruteLeftSide, intVars, strVars, boolVars);
        String val2 = resolveValue(bruteRightSide, intVars, strVars, boolVars);

        boolean passInIf;
        try {
            int num1 = Integer.parseInt(val1);
            int num2 = Integer.parseInt(val2);
            passInIf = calcComparative(num1, operator, num2);
        } catch (NumberFormatException e) {
            if (operator.equals("=") || operator.equals("==")) {
                passInIf = val1.equals(val2);
            } else if (operator.equals("!=")) {
                passInIf = !val1.equals(val2);
            } else {
                passInIf = false;
            }
        }

        if (passInIf) {
            executeCommandBlock(commandThen);
            return true;
        }

        Pattern elifPattern = Pattern.compile("elif\\s?\\(check=\\{([^\\s}]+)\\s?([><=!]+|=)\\s?([^\\s}]+)\\},\\s?then=\\{");
        Matcher elifMatcher = elifPattern.matcher(commandBlockRest);

        if (elifMatcher.lookingAt()) {
            String eVal1 = resolveValue(elifMatcher.group(1), intVars, strVars, boolVars);
            String eOperator = elifMatcher.group(2);
            String eVal2 = resolveValue(elifMatcher.group(3), intVars, strVars, boolVars);

            boolean passInElif;
            try {
                int eNum1 = Integer.parseInt(eVal1);
                int eNum2 = Integer.parseInt(eVal2);
                passInElif = calcComparative(eNum1, eOperator, eNum2);
            } catch (NumberFormatException e) {
                if (eOperator.equals("=") || eOperator.equals("==")) {
                    passInElif = eVal1.equals(eVal2);
                } else if (eOperator.equals("!=")) {
                    passInElif = !eVal1.equals(eVal2);
                } else {
                    passInElif = false;
                }
            }

            int fimDoElifThen = elifMatcher.end();
            Object[] resultadoElif = separarBlocoPorContador(commandBlockRest, fimDoElifThen);
            String eCommandThen = (String) resultadoElif[0];
            int fimDoBlocoElifNaLinha = (int) resultadoElif[1];

            if (passInElif) {
                executeCommandBlock(eCommandThen);
                return true;
            }

            commandBlockRest = commandBlockRest.substring(fimDoBlocoElifNaLinha).trim();
        }

        if (commandBlockRest.startsWith("else={")) {
            int inicioBlocoElse = commandBlockRest.indexOf("{") + 1;
            Object[] resultadoElse = separarBlocoPorContador(commandBlockRest, inicioBlocoElse);
            String comandoElse = (String) resultadoElse[0];

            executeCommandBlock(comandoElse);
            return true;
        }

        return false;
    }

    private static void executeCommandBlock(String bruteBlock) {
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

    private static Object[] separarBlocoPorContador(String texto, int indexInicio) {
        int contadorChaves = 1;
        StringBuilder bloco = new StringBuilder();
        int i = indexInicio;

        while (i < texto.length() && contadorChaves > 0) {
            char atual = texto.charAt(i);
            if (atual == '{') contadorChaves++;
            else if (atual == '}') contadorChaves--;

            if (contadorChaves == 0) break;

            bloco.append(atual);
            i++;
        }

        if (contadorChaves != 0) {
            System.out.println("\u001B[31m[JEE Syntax Error] A brace was not closed in the block\u001B[0m");
            return new Object[]{ "", texto.length() };
        }

        return new Object[]{ bloco.toString().trim(), i + 1 };
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

    private static String resolveValue(String token, Map<String, Integer> intVars, Map<String, String> strVars, Map<String, String> boolVars) {
        String cleanToken = token.trim();

        if (cleanToken.startsWith("$@")) {
            String nameVar = cleanToken.substring(2);
            if (strVars.containsKey(nameVar)) {
                return strVars.get(nameVar);
            }
            if (boolVars.containsKey(nameVar)) {
                return boolVars.get(nameVar);
            }
            if (intVars.containsKey(nameVar)) {
                return String.valueOf(intVars.get(nameVar));
            }
            return "";
        }

        if (cleanToken.startsWith("\"") && cleanToken.endsWith("\"")) {
            return cleanToken.substring(1, cleanToken.length() - 1);
        }

        return cleanToken;
    }
}


