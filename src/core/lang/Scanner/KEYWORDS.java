package Scanner;

import java.util.HashMap;
import java.util.Map;

import static Scanner.TokenType.*;

public class KEYWORDS {
    public static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("et",    AND);         // "and"
        keywords.put("classe",  CLASS);       // "class"
        keywords.put("sinon",   ELSE);        // "else"
        keywords.put("faux",  FALSE);       // "false"
        keywords.put("pour",    FOR);         // "for"
        keywords.put("fon",    FUN);         // "fun"
        keywords.put("si",     IF);          // "if"
        keywords.put("nul",    NIL);         // "nil"
        keywords.put("ou",     OR);          // "or"
        keywords.put("print",  PRINT);       // "imprimer"
        keywords.put("retourne", RETURN);      // "return"
        keywords.put("super",  SUPER);       // "super"
        keywords.put("this",   THIS);        // "ceci"
        keywords.put("vrai",   TRUE);        // "true"
        keywords.put("var",    VAR);         // "var"
        keywords.put("tant_que",  WHILE);       // "while"
    }

}
