package Scanner;

import java.util.HashMap;
import java.util.Map;

import static Scanner.TokenType.*;
import static Utils.CONSTANT.*;

public class KEYWORDS {
    public static final Map<String, TokenType> keywords;
    public static final Map<String, String> e_keywords;

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
    static {
        e_keywords = new HashMap<>();
        e_keywords.put(K_AND,    "and");
        e_keywords.put(K_CLASS,  "class");
        e_keywords.put(K_ELSE,   "else");
        e_keywords.put(K_FALSE,  "false");
        e_keywords.put(K_FOR,    "for");
        e_keywords.put(K_FUN,    "fon");
        e_keywords.put(K_IF,     "if");
        e_keywords.put(K_NIL,    "null");
        e_keywords.put(K_OR,     "or");
        e_keywords.put(K_PRINT,  "afficher");
        e_keywords.put(K_RETURN, "retourner");
        e_keywords.put(K_SUPER,  "super");
        e_keywords.put(K_THIS,   "this");
        e_keywords.put(K_TRUE,   "true");
        e_keywords.put(K_VAR,    "var");
        e_keywords.put(K_WHILE,  "while");
    }
}
