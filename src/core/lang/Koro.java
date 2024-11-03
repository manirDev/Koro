import java.io.IOException;

import static Run.Run.fileRunner;
import static Run.Run.promptRunner;

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
