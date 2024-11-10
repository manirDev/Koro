package Error;

import Scanner.Token;
import Scanner.TokenType;

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

    public static void error(Token token, String message){
        if (token.tokenType == TokenType.EOF){
            report(token.linePos, " a la fin ", message);
        }
        else{
            report(token.linePos, " a '" + token.lexeme + "'", message);
        }
    }
}
