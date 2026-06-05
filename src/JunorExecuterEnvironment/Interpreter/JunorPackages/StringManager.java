package JunorExecuterEnvironment.Interpreter.JunorPackages;

import JunorExecuterEnvironment.Interpreter.ANSIColors;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {

    private static final Pattern varPattern = Pattern.compile("\\$@([a-zA-Z_0-9]+)");

    public static void Show(Matcher groupShow, Map<String, String> strVars, Map<String, Integer> intVars, Map<String, String> boolVars) {
        String textoBruto = groupShow.group(1);

        String textoFinal = varPattern.matcher(textoBruto).replaceAll(matchResult -> {
            String nomeVar = matchResult.group(1);

            if (strVars.containsKey(nomeVar)) return strVars.get(nomeVar);
            if (intVars.containsKey(nomeVar)) return String.valueOf(intVars.get(nomeVar));
            if (boolVars.containsKey(nomeVar)) return boolVars.get(nomeVar);

            return matchResult.group(0);
        });

        System.out.println(textoFinal);
    }
    public static void Inputer(Matcher inputerGroup,Map<String, String> strVars, Map<String, Integer> intVars) {
        String textoBruto = inputerGroup.group(3);

        String textoFinal = varPattern.matcher(textoBruto).replaceAll(matchResult -> {
            String nomeVar = matchResult.group(1);

            if (strVars.containsKey(nomeVar)) return strVars.get(nomeVar);
            if (intVars.containsKey(nomeVar)) return String.valueOf(intVars.get(nomeVar));

            return matchResult.group(0);
        });

        Scanner scanner = new Scanner(System.in);
        System.out.print(textoFinal);
        String value = scanner.nextLine();
        System.out.print("\n");

        if (inputerGroup.group(1).equals("int")) {
            try {
                intVars.put(inputerGroup.group(2), Integer.parseInt(value));
            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[Junor Error] this int '" + value + "' is incompatible" + ANSIColors.ANSI_RESET);
            }
        }

        if (inputerGroup.group(1).equals("str")) {
            strVars.put(inputerGroup.group(2), value);
        }
    }
}
