package Scanner;

import java.util.HashMap;
import java.util.Map;

import static Scanner.TokenType.*;
import static Utils.CONSTANT.*;

public class KEYWORDS {
    public static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put(K_AND,    AND);
        keywords.put(K_CLASS,  CLASS);
        keywords.put(K_ELSE,   ELSE);
        keywords.put(K_FALSE,  FALSE);
        keywords.put(K_FOR,    FOR);
        keywords.put(K_FUN,    FUN);
        keywords.put(K_IF,     IF);
        keywords.put(K_NIL,    NIL);
        keywords.put(K_OR,     OR);
        keywords.put(K_PRINT,  PRINT);
        keywords.put(K_RETURN, RETURN);
        keywords.put(K_SUPER,  SUPER);
        keywords.put(K_THIS,   THIS);
        keywords.put(K_TRUE,   TRUE);
        keywords.put(K_VAR,    VAR);
        keywords.put(K_WHILE,  WHILE);
    }

}
