import java.io.IOException;

import static Runner.Runner.fileRunner;
import static Runner.Runner.promptRunner;

public class Koro {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Utilisation: Koro [Script]");
            System.exit(64);
        }
        else if (args.length == 1) {
            fileRunner(args[0]);
        }
        else{
            promptRunner();
        }
    }
}
