package Error;

import Scanner.Token;

public class ParseError extends RuntimeException{
    public static ParseError error(Token token, String message){
        Error.error(token, message);
        return new ParseError();
    }
}
