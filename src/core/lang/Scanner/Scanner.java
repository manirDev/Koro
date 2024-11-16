package Scanner;


import java.util.ArrayList;
import java.util.List;

import static Error.Error.error;
import static Scanner.KEYWORDS.keywords;
import static Scanner.TokenType.*;
import static Utils.CONSTANT.*;

public class Scanner {
    private final String sourceCode;
    private static final List<Token> tokens = new ArrayList<Token>();

    private int startPos = 0;
    private int currenPos = 0;
    private int currenLine = 1;

    public Scanner(String sourceCode){
        this.sourceCode = sourceCode;
    }

    public List<Token> tokenize(){
        while(!isAtEnd()){
            startPos = currenPos;
            scanToken();
        }

        //to make the parser cleaner, not strictly needed.
        tokens.add(new Token(EOF, "", null, currenLine));
        return tokens;
    }
    private void scanToken(){
        char currenChar = advance();
        switch (currenChar){
            // Single-character tokens.
            case '(' :
                pushToken(OPEN_PAREN);
                break;
            case ')' :
                pushToken(CLOSE_PAREN);
                break;
            case '{' :
                pushToken(OPEN_BRACE);
                break;
            case '}' :
                pushToken(CLOSE_BRACE);
                break;
            case ',' :
                pushToken(COMMA);
                break;
            case '.' :
                pushToken(DOT);
                break;
            case '-' :
                pushToken(MINUS);
                break;
            case '+' :
                pushToken(PLUS);
                break;
            case ';' :
                pushToken(SEMICOLON);
                break;
            case '*' :
                pushToken(STAR);
                break;
            case '!' :
                pushToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=' :
                pushToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<' :
                pushToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>' :
                pushToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                /*
                    That character needs a little special handling because
                    comments begin with a slash too.
                 */
                if (match('/')){
                    //a comment goes until the end of the line
                    //skipping a comment
                    while (peek() != NEW_LINE && !isAtEnd()){
                        advance();
                    }
                }
                else{
                    pushToken(SLASH);
                }
                break;
            case ' ':
            case '\t':
            case '\r':
                break;
            case '\n':
                currenLine++;
                break;
            case '"':
                stringHandler();
                break;
            default:
                if (isDigit(currenChar)){
                    numberHandler();
                }
                else if(isAlpha(currenChar)){
                    identifierHandler();
                }
                else {
                /*
                    What happens if a user throws a source file containing
                    some characters Koro does not use to, like @#^.
                 */
                    error(currenLine, "Caractère inattendu");
                }
                break;
        }
    }

    private void stringHandler(){
        while (peek() != STRING_QUOTE && !isAtEnd()){
            if (peek() == NEW_LINE){
                currenLine++;
            }
            advance();
        }
        //hitting end of the source code without seeing an ending quote of the string
        if (isAtEnd()){
            error(currenLine, "Chaîne non terminée.");
            return;
        }

        advance();

        //get the surrounding quotes
        String rawValue = sourceCode.substring(startPos + 1, currenPos - 1);
        String value = unescapeString(rawValue);
        push(STRING, value);
    }
    private String unescapeString(String rawString) {
        return rawString
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
    private void numberHandler(){
        while (isDigit(peek())){
            advance();
        }

        //Look for a fractional part.
        if (peek() == FRACTIONAL_PERIOD && isDigit(peekNext())){
            advance();
            while (isDigit(peek())){
                advance();
            }
        }
        push(NUMBER, Double.parseDouble(sourceCode.substring(startPos, currenPos)));
    }

    private void identifierHandler(){
        while (isAlphaNumeric(peek())){
            advance();
        }

        String key = sourceCode.substring(startPos, currenPos);
        TokenType value = keywords.get(key);
        if (value == null){
            value = IDENTIFIER;
        }

        pushToken(value);
    }
    private boolean match(char expected){
        // no next char to consume
        if (isAtEnd()){
            return false;
        }
        if (sourceCode.charAt(currenPos) != expected){
            return false;
        }
        currenPos++;
        return true;
    }

    private void pushToken(TokenType tokenType){
        push(tokenType, null);
    }

    private void push(TokenType tokenType, Object literal){
        String value = sourceCode.substring(startPos, currenPos);
        tokens.add(new Token(tokenType, value, literal, currenLine));
    }

    private char advance(){
        char currChar = sourceCode.charAt(currenPos);
        currenPos++;
        return currChar;
    }

    private char peek(){
        if (isAtEnd()){
            return TERMINATOR;
        }
        char currChar = sourceCode.charAt(currenPos);
        return currChar;
    }
    private char peekNext(){
        if (currenPos + 1 >= sourceCode.length()){
            return TERMINATOR;
        }
        char nextChar = sourceCode.charAt(currenPos + 1);
        return nextChar;
    }
    private boolean isAtEnd(){
        return this.currenPos >= this.sourceCode.length();
    }

    private boolean isAlphaNumeric(char currenChar){
        return isAlpha(currenChar) || isDigit(currenChar);
    }

    private boolean isDigit(char currenChar){
        return (
                currenChar >= DIGIT_LOWER_BOUND
                        &&
                currenChar <= DIGIT_UPPER_BOUND);
    }

    private boolean isAlpha(char currenChar){
        boolean lowerCase = (
                currenChar >= LOWER_CASE_CHAR_LOWER_BOUND
                        &&
                currenChar <= LOWER_CASE_CHAR_UPPER_BOUND);
        boolean upperCase = (
                currenChar >= UPPER_CASE_CHAR_LOWER_BOUND
                        &&
                currenChar <= UPPER_CASE_CHAR_UPPER_BOUND);
        boolean underScore = (currenChar == UNDER_SCORE);
        return lowerCase || upperCase || underScore;
    }
}
