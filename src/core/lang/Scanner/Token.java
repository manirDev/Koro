package Scanner;

public class Token {
    public final TokenType tokenType;
    public final String lexeme;
    public final Object literal;
    public final int linePos;

    public Token(TokenType tokenType, String lexeme, Object literal, int linePos){
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
        this.linePos = linePos;
    }

    public String toString() {
        return String.format("Token {value, type, literal} : {%s, %s, %s}", lexeme, tokenType, literal);
    }
}
