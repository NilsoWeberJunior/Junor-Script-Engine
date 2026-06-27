package JunorExecuterEnvironment;

public class JAEE {
    public static void main(String[] args) {

        if (args.length > 0) {
            String archiveLocation = args[0];
            try {
                java.io.File archiveToOpen = new java.io.File(archiveLocation);
                if (archiveToOpen.exists()) {
                    JAFEE.OpenArchive(archiveToOpen.toURI());
                    return;
                }
            } catch (Exception e) {}
        }
    }
}
