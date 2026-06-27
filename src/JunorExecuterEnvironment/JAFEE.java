package JunorExecuterEnvironment;

import JunorExecuterEnvironment.Interpreter.ANSIColors;
import JunorExecuterEnvironment.Interpreter.JunorCommands;

import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;

public class JAFEE {
    private static Path Manifest = null;
    private static Path ScriptToHead = null;
    private static boolean InHeader = false;
    private static boolean ManifestVersionAccepted = false;
    private static String ScriptAuthor = null;
    private static boolean ScriptRun = false;

    public static void OpenArchive(URI archiveToOpenURI) {
        JunorCommands.clearMemory();

        Manifest = null;
        ScriptRun = false;
        ScriptAuthor = null;
        InHeader = false;
        ManifestVersionAccepted = false;

        System.out.println("=====================START SCRIPT=====================\n");

        try {

            Path CJunorAppLocation = Paths.get(archiveToOpenURI).toAbsolutePath();

            URI CJunorAppUri = URI.create("jar:" + CJunorAppLocation.toUri().toString());


            try (FileSystem CJunorAppFile = FileSystems.newFileSystem(CJunorAppUri, new HashMap<>())) {
                Manifest = CJunorAppFile.getPath("/manifest.jmtdt");
                if (!Files.exists(Manifest)) {
                    System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Critical Error! manifest not found." + ANSIColors.ANSI_RESET);
                    return;
                }
                ScriptToHead = CJunorAppFile.getPath("/main.junor");
                if (!Files.exists(ScriptToHead)) {
                    System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Critical error! main.junor not found" + ANSIColors.ANSI_RESET);
                    return;
                }

                java.util.List<String> manifestLines = Files.readAllLines(Manifest);
                for (String manifestLine : manifestLines) {
                    HeadManifest(manifestLine);
                }

            } catch (Exception e) {
                System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Error to head archive! " + e + ANSIColors.ANSI_RESET);
            }
        } catch (Throwable t) {
            System.out.println(ANSIColors.ANSI_RED + "[Junor Loader] Error to init a script engine! error code: " + t.getMessage() + ANSIColors.ANSI_RESET);
        }

        JunorCommands.clearMemory();

        System.out.println("\n======================END SCRIPT======================\n");
    }

    private static void HeadManifest(String manifestLine) {
        String line = manifestLine.trim();

        if (line.equals("{header>")) {
            InHeader = true;
            System.out.println("[=======STARTINFO=======]\n");
        } else if (line.equals("<header}")) {
            InHeader = false;

            if (ScriptAuthor != null && ManifestVersionAccepted == true) {

                if (!ScriptRun) {
                    try {
                        System.out.println("\n[========ENDINFO========]\n");

                        java.util.List<String> scriptLines = Files.readAllLines(ScriptToHead);

                        for (String scriptLine : scriptLines) {
                            HeadScript(scriptLine);
                        }

                    } catch (Exception e) {
                        System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Erro to head script 'main.junor': " + e.getMessage() + ANSIColors.ANSI_RESET);
                    }

                    ScriptRun = true;
                }

            }

        } else if (InHeader == true && line.startsWith("[version=0.0.5]")) {
            ManifestVersionAccepted = true;
            System.out.println(ANSIColors.ANSI_GREEN + "[Junor Info] Manifest version accepted" + ANSIColors.ANSI_RESET);

        } else if (InHeader == true && line.startsWith("[author=\"") && line.endsWith("\"]")) {
            ScriptAuthor = line.replace("[author=\"", "").replace("\"]", "");
            System.out.println(ANSIColors.ANSI_GREEN + "[Junor Info] author version accepted" + ANSIColors.ANSI_RESET);

        } else if (line.isEmpty()) return;

        else {
            System.out.println(ANSIColors.ANSI_RED + "[Junor Error] Error to head a header!" + ANSIColors.ANSI_RESET);
        }
    }

    private static void HeadScript(String ScriptLine) {
        String scriptLine = ScriptLine.trim();
        JunorCommands.Interpret(scriptLine);

    }

    public static void CreateArchiveCJunorApp(URI targetCJunorAppURI, Path originalScriptPath, String author) {
        System.out.println(ANSIColors.ANSI_BLUE + "[Junor Compiler] Starting compilation process..." + ANSIColors.ANSI_RESET);

        java.util.Map<String, String> env = new HashMap<>();
        env.put("create", "true");

        URI CJunorAppUri = URI.create("jar:" + targetCJunorAppURI.toString());

        try {
            Path targetPath = Paths.get(targetCJunorAppURI);
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }

            try (FileSystem newCJunorAppFile = FileSystems.newFileSystem(CJunorAppUri, env)) {

                Path manifestPathInZip = newCJunorAppFile.getPath("/manifest.jmtdt");

                java.util.List<String> manifestContent = java.util.Arrays.asList(
                        "{header>",
                        "[version=0.0.5]",
                        "[author=\"" + author + "\"]",
                        "<header}"
                );

                Files.write(manifestPathInZip, manifestContent, java.nio.charset.StandardCharsets.UTF_8);
                System.out.println(ANSIColors.ANSI_GREEN + "[Junor Compiler] manifest.jmtdt generated successfully." + ANSIColors.ANSI_RESET);

                Path scriptPathInZip = newCJunorAppFile.getPath("/main.junor");

                if (!Files.exists(originalScriptPath)) {
                    System.out.println(ANSIColors.ANSI_RED + "[Junor Compiler Error] Source script 'main.junor' not found at target path!" + ANSIColors.ANSI_RESET);
                    return;
                }

                Files.copy(originalScriptPath, scriptPathInZip, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(ANSIColors.ANSI_GREEN + "[Junor Compiler] main.junor successfully packaged." + ANSIColors.ANSI_RESET);

            }

            System.out.println(ANSIColors.ANSI_GREEN + "\n[Junor Compiler] App compiled successfully target: " + targetPath.getFileName() + ANSIColors.ANSI_RESET);

        } catch (Exception e) {
            System.out.println(ANSIColors.ANSI_RED + "[Junor Compiler Error] Failed to pack app archive: " + e.getMessage() + ANSIColors.ANSI_RESET);
        }
    }
}