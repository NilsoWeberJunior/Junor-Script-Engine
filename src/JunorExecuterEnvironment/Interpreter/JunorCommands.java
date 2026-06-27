package JunorExecuterEnvironment.Interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JunorExecuterEnvironment.Interpreter.JunorPackages.IfsAndElsesManagement;
import JunorExecuterEnvironment.Interpreter.JunorPackages.IntegerManagement;
import JunorExecuterEnvironment.JAFEE;
import JunorExecuterEnvironment.JFEE;
import JunorExecuterEnvironment.Interpreter.JunorPackages.StringManager;

public class JunorCommands {
    private static final Pattern strVarPattern = Pattern.compile("^strVar\\s?\\(name=\\(\"(\\w+)\"\\),\\s?value=\\(\"([^\"]+)\"\\)\\)");
    private static final Pattern intVarPattern = Pattern.compile("^intVar\\s?\\(name=\\(\"(\\w+)\"\\),\\s?value=\\((\\d+)\\)\\)");
    private static final Pattern boolVarPattern = Pattern.compile("^boolVar\\s?\\(name=\\(\"(\\w+)\"\\),\\s?value=\\((true|false)\\)\\)");
    private static final Pattern showCommandPattern = Pattern.compile("^show\\s?\\(\"(.+?)\"\\)");
    private static final Pattern runCommandPattern = Pattern.compile("^run\\s?\\(\\[\"(.+?)\"]\\)");
    private static final Pattern createCJunorCommandPattern = Pattern.compile("^createCJunor\\s?\\(CJunorLocation=\\[\"(.*?)\"],\\s?originalJunor=\\[\"(.*?)\"],\\s?scriptAuthor=\\(\"(.*?)\"\\)\\)");
    private static final Pattern createCJunorAppCommandPattern = Pattern.compile("^createCJunorApp\\s?\\(CJunorAppLocation=\\[\"(.*?)\"],\\s?originalJunor=\\[\"(.*?)\"],\\s?appAuthor=\\(\"(.*?)\"\\)\\)");
    private static final Pattern runAppCommandPattern = Pattern.compile("^run\\s?\\(\\[\"(.+?)\"]\\)");
    private static final Pattern calcCommandPattern = Pattern.compile("^calc\\s?\\(target=\\(\"(\\w+)\"\\),\\s?calc=\\(([^\\s)]+)\\s?([+\\-x])\\s?([^\\s)]+)\\),\\s?print=\\((true|false)\\)\\)");
    private static final Pattern inputCommandPattern = Pattern.compile("^input\\s?\\(type=\\((int|str)\\),\\s?target=\\(\"(\\w+)\"\\),\\s?msg=\\(\"(.+?)\"\\)\\)");
    private static final Pattern ifCommandPattern = Pattern.compile("^if\\s?\\(check=\\{([^\\s)]+)\\s?([><=!]+|=)\\s?([^\\s)]+)\\},\\s?then=\\{");
    private static final Pattern forCommandPattern = Pattern.compile("^for\\s?\\(var=\\(\"(\\w+)\"\\),\\s?range=\\(([^\\s)]+)\\s?to\\s?([^\\s)]+)\\),\\s?do=\\{");
    private static final Pattern whileCommandPattern = Pattern.compile("^while\\s?\\(check=\\{([^\\s}]+)\\s?([><=!]+|=)\\s?([^\\s}]+)\\},\\s?do=\\{");
    private static final Pattern funcDefPattern = Pattern.compile("^func\\s?\\(name=\\(\"(\\w+)\"\\),\\s?body=\\{");
    private static final Pattern funcCallPattern = Pattern.compile("^call\\s?\\(name=\\(\"(\\w+)\"\\)\\)");


    private static Map<String, Integer> intVars = new HashMap<>();
    private static Map<String, String> strVars = new HashMap<>();
    private static Map<String, String> boolVars = new HashMap<>();
    private static Map<String, String> funcRegistry = new HashMap<>();


    public static void Interpret(String commandToInterpret) {

        String line = commandToInterpret.trim();
        if (line.startsWith("#/") || line.isEmpty()) return;

        Matcher matcher;

        if ((matcher = strVarPattern.matcher(line)).matches()) {
            strVars.put(matcher.group(1), matcher.group(2));
            return;
        }

        if ((matcher = intVarPattern.matcher(line)).matches()) {
            intVars.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
            return;
        }

        if ((matcher = showCommandPattern.matcher(line)).matches()) {
            StringManager.Show(matcher, strVars, intVars, boolVars);
            return;
        }

        if ((matcher = boolVarPattern.matcher(line)).matches()) {
            boolVars.put(matcher.group(1), matcher.group(2));
            return;
        }

        if (line.startsWith("#getVersion")) {
            System.out.println("VERSION GET STARTED");
            System.out.println("Junor Interpreter version: 0.0.5");
            System.out.println("Junor Manifest version: 0.0.5");
            System.out.println("Junor Application Interpreter: 0.0.5");
            System.out.println("Junor Application Manifest: 0.0.5");
            System.out.println("VERSION GET FINISHED");
            return;
        }

        if ((matcher = runCommandPattern.matcher(line)).matches()) {
            String archivePatch = matcher.group(1);
            try {
                java.io.File archive = new java.io.File(archivePatch);
                if (archive.exists()) {
                    System.out.println(ANSIColors.ANSI_BLUE + "[JEE Loader] Loading .CJunor package..." + ANSIColors.ANSI_RESET);
                    JFEE.OpenArchive(archive.toURI());
                } else {
                    System.out.println(ANSIColors.ANSI_RED + "[JEE Error] archive not found: " + archivePatch + ANSIColors.ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[JEE Error] Error to process archive: " + e.getMessage() + ANSIColors.ANSI_RESET);
            }
            return;
        }
        if ((matcher = runAppCommandPattern.matcher(line)).matches()) {
            String archivePatch = matcher.group(1);
            try {
                java.io.File archive = new java.io.File(archivePatch);
                if (archive.exists()) {
                    System.out.println(ANSIColors.ANSI_BLUE + "[JEE Loader] Loading .CJunorApp package..." + ANSIColors.ANSI_RESET);
                    JAFEE.OpenArchive(archive.toURI());
                } else {
                    System.out.println(ANSIColors.ANSI_RED + "[JEE Error] archive not found: " + archivePatch + ANSIColors.ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[JEE Error] Error to process archive: " + e.getMessage() + ANSIColors.ANSI_RESET);
            }
            return;
        }

        if ((matcher = inputCommandPattern.matcher(line)).matches()) {
            StringManager.Inputer(matcher, strVars, intVars);
            return;
        }


        if ((matcher = calcCommandPattern.matcher(line)).matches()) {
            IntegerManagement.calc(matcher, intVars);
            return;
        }

        if ((matcher = ifCommandPattern.matcher(line)).lookingAt()) {
            IfsAndElsesManagement.evaluateIf(matcher, intVars, strVars, boolVars, line);
            return;
        }

        if ((matcher = forCommandPattern.matcher(line)).lookingAt()) {
            int fimDoMatchRegex = matcher.end();
            String blocoLoop = extractBlock(line, fimDoMatchRegex);

            JunorExecuterEnvironment.Interpreter.JunorPackages.LoopsManagement.executeFor(matcher, intVars, blocoLoop);
            return;
        }

        if ((matcher = whileCommandPattern.matcher(line)).lookingAt()) {
            int fimDoMatchRegex = matcher.end();
            String blocoLoop = extractBlock(line, fimDoMatchRegex);

            JunorExecuterEnvironment.Interpreter.JunorPackages.LoopsManagement.executeWhile(matcher, intVars, blocoLoop);
            return;
        }

        if ((matcher = funcDefPattern.matcher(line)).lookingAt()) {
            int fimDoMatchRegex = matcher.end();
            String corpoFuncao = extractBlock(line, fimDoMatchRegex);

            funcRegistry.put(matcher.group(1), corpoFuncao);
            return;
        }

        if ((matcher = funcCallPattern.matcher(line)).matches()) {
            String nameFunc = matcher.group(1);
            String commands = funcRegistry.get(nameFunc);

            if (commands != null) {
                int nivelChaves = 0;
                StringBuilder comandoAtual = new StringBuilder();

                for (int i = 0; i < commands.length(); i++) {
                    char c = commands.charAt(i);

                    if (c == '{') nivelChaves++;
                    else if (c == '}') nivelChaves--;

                    if (c == '|' && nivelChaves == 0) {
                        Interpret(comandoAtual.toString().trim());
                        comandoAtual.setLength(0);
                    } else {
                        comandoAtual.append(c);
                    }
                }
                if (comandoAtual.length() > 0) {
                    Interpret(comandoAtual.toString().trim());
                }
            }
            return;
        }

        if ((matcher = createCJunorCommandPattern.matcher(line)).matches()) {
            String CJunorLocation = matcher.group(1);
            String junorFileLocation = matcher.group(2);
            String scriptAuthor = matcher.group(3);

            try {
                if (!CJunorLocation.toLowerCase().endsWith(".cjunor")) {
                    CJunorLocation = CJunorLocation + ".CJunor";
                }

                java.io.File CJunorArchive = new java.io.File(CJunorLocation);
                java.io.File JunorArchive = new java.io.File(junorFileLocation);

                if (!CJunorArchive.isAbsolute()) {
                    String userHome = System.getProperty("user.home");
                    CJunorArchive = new java.io.File(userHome + "/Desktop/" + CJunorLocation);
                }

                JFEE.CreateArchiveCJunor(
                        CJunorArchive.toURI(),
                        JunorArchive.toPath(),
                        scriptAuthor
                );
            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[JEE Error] Error to pack archive: " + e.getMessage() + ANSIColors.ANSI_RESET);
            }
            return;
        }

        if ((matcher = createCJunorAppCommandPattern.matcher(line)).matches()) {
            String CJunorAppLocation = matcher.group(1);
            String junorFileLocation = matcher.group(2);
            String appAuthor = matcher.group(3);

            try {
                if (!CJunorAppLocation.toLowerCase().endsWith(".cja")) {
                    CJunorAppLocation = CJunorAppLocation + ".CJA";
                }

                java.io.File CJunorAppArchive = new java.io.File(CJunorAppLocation);
                java.io.File JunorArchive = new java.io.File(junorFileLocation);

                if (!CJunorAppArchive.isAbsolute()) {
                    String userHome = System.getProperty("user.home");
                    CJunorAppArchive = new java.io.File(userHome + "/Desktop/" + CJunorAppLocation);
                }

                JFEE.CreateArchiveCJunor(
                        CJunorAppArchive.toURI(),
                        JunorArchive.toPath(),
                        appAuthor
                );
            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[JEE Error] Error to pack app archive: " + e.getMessage() + ANSIColors.ANSI_RESET);
            }
            return;
        }

        if (line.equals("help")) {
            System.out.println(ANSIColors.ANSI_BLUE +

                    "\nCOMMANDS:\n" +
                    "help - list all comands\n" +
                    "strVar(name=(\"variable_name\"), value=(\"variable_value_text\")) - creates a variable type: string\n" +
                    "intVar(name=(\"variable_name\"), value=(variable_value_int)) - creates a variable type: int\n" +
                    "boolVar(name=(\"variable_name\"), value=(variable_value_false_and_true)) - creates a variable type: boolean\n" +
                    "show(\"text\") - prints the text between parentheses\n" +
                    "run([\"archive_cjunor_location\"]) - run a compiled junor archive\n" +
                    "input(type=(int|str), target=(\"var_name\"), msg=(\"text\")) - requests a user input via keyboard\n" +
                    "createCJunor(CJunorLocation=[\"cjunor_archive_destination\"], originalJunor=[\"junor_file_location\"], scriptAuthor=(\"text\")) - creates a compiled junor archive\n" +
                    "if(check={val op val}, then={cmd}) elif(check={val op val}, then={cmd}) else={cmd} - verify conditions (if/elif/else)\n" +
                    "calc(target=(\"var_name\"), calc=(num1, oper, num2), print=(true|false)) - prints the result of a mathematical operation (and save a var)\n" +
                    "for(var=(\"var_name\"), range=(start to end), do={cmd}) - loops execution from start to end (Accepts ints, variables, and multiple commands via '|'\n" +
                    "while(check={val op val}, do={cmd}) - repeats execution while condition is true (Accepts variables and '|' via 'do')\n" +
                    "func(name=(\"func_name\"), body={cmd|cmd}) - defines a reusable block of commands\n" +
                    "call(name=(\"func_name\")) - executes a defined function\n" +
                    "createCJunorApp(CJunorAppLocation=[\"cjunorapp_archive_destination\"], originalJunor=[\"junor_file_location\"], appAuthor=(\"text\")) - creates a compiled junor application archive\n" +
                    "runApp([\"archive_cjunorapp_location\"]) - run a compiled junor application archive\n" +
                    "exit - exit of interpreter\n" +

                    "\nSYSTEM TAGS\n" +
                    "#/ - commentary\n" +
                    "#getVersion - get version of Junor\n" +

                    "\nOTHERS:\n" +
                    "{} - command execution\n" +
                    "[] - archive location\n" +
                    "() - value\n" +
                    "| - adds a line break for statement blocks in if, else, else if, function, for, and while constructs, ex: if(check={i = 5}, then={show(\"hello, world\") | show(\"hello!\")})" +
                    ANSIColors.ANSI_RESET +
                    "\n");
            return;
        }


        System.out.println(ANSIColors.ANSI_RED + "[JEE Syntax Error] Invalid Command: " + line + ANSIColors.ANSI_RESET);
        System.out.flush();
    }
    public static void clearMemory() {
        intVars.clear();
        strVars.clear();
        boolVars.clear();
    }

    private static String extractBlock(String linha, int indexInicio) {
        int contadorChaves = 1;
        StringBuilder bloco = new StringBuilder();
        int i = indexInicio;

        while (i < linha.length() && contadorChaves > 0) {
            char atual = linha.charAt(i);
            if (atual == '{') contadorChaves++;
            else if (atual == '}') contadorChaves--;

            if (contadorChaves == 0) break;

            bloco.append(atual);
            i++;

        }

        if (contadorChaves != 0) {
            throw new RuntimeException(ANSIColors.ANSI_RED + "[JEE Error] Syntax Error: Mismatched brackets '{}'" + ANSIColors.ANSI_RESET);
        }
        return bloco.toString().trim();
    }
}
