package Error;

import Scanner.Token;

public class RuntimeError extends RuntimeException{
    final Token token;
    public static boolean hadRuntimeError = false;

    public RuntimeError(Token token, String message){
        super(message);
        this.token = token;
    }

    public static void runtimeError(RuntimeError error){
        System.err.println(error.getMessage() +
                           "\n[ligne " + error.token.linePos + "]");
        hadRuntimeError = true;
    }
}
