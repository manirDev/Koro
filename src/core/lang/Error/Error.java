package Error;

public class Error {
    public static boolean hadError = false;
    public static void error(int line, String message){
        report(line, "", message);
    }
    private static void report(int line, String where, String message){
        System.err.println(
                "[ligne " + line + "] Erreur à " + where + ": " + message
        );
        hadError = true;
    }
}
