package Parser;

import Ast.Expression.*;
import Ast.Statement.Expression;
import Ast.Statement.Print;
import Ast.Statement.Stmt;
import Ast.Statement.Var;
import Scanner.Token;
import Scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

import Error.ParseError;
import static Error.ParseError.error;
import static Scanner.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int currenPos = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Stmt> parse(){
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()){
            statements.add(declaration());
        }
        return statements;
    }

    private Expr expression(){
        return equality();
    }

    private Stmt declaration(){
        try{
            if (match(VAR)){
                return varDeclaration();
            }
            return statement();
        }
        catch (ParseError error){
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration(){
        Token name = consume(IDENTIFIER, "Attend nom du variable");
        Expr initializer = null;
        if (match(EQUAL)){
            initializer = expression();
        }
        consume(SEMICOLON, "Attendu ';' apres la declaration du variable");
        return new Var(name, initializer);
    }

    private Stmt statement(){
        if (match(PRINT)){
            return printStatement();
        }
        return expressionStatement();
    }

    private Stmt printStatement(){
        Expr value = expression();
        consume(SEMICOLON, "Attendu ';' apres une valeur");
        return new Print(value);
    }

    private Stmt expressionStatement(){
        Expr expr = expression();
        consume(SEMICOLON, "Attendu ';' apres une expression");
        return new Expression(expr);
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return  expr;
    }

    private Expr comparison(){
        Expr expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            Token operator = previous();
            Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term(){
        Expr expr = factor();
        while (match(MINUS, PLUS)){
            Token operator = previous();
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor(){
        Expr expr = unary();
        while (match(SLASH, STAR)){
            Token operator = previous();
            Expr right = unary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary(){
        if (match(BANG, MINUS)){
            Token operator = previous();
            Expr right = unary();
            return new Unary(operator, right);
        }
        return primary();
    }

    private Expr primary(){
        if (match(FALSE)){
            return new Literal(false);
        }
        if (match(TRUE)){
            return new Literal(true);
        }
        if (match(NIL)){
            return new Literal(null);
        }

        if (match(NUMBER, STRING)){
            return new Literal(previous().literal);
        }
        if (match(OPEN_PAREN)){
            Expr expr = expression();
            consume(CLOSE_PAREN, "Un ')' est attendu après l'expression.");
            return new Grouping(expr);
        }
        if (match(IDENTIFIER)){
            return new Variable(previous());
        }
        throw error(peek(), "Est attendu une expression");
    }

    private void synchronize(){
        advance();
        while (!isAtEnd()){
            if (previous().tokenType == SEMICOLON){
                return;
            }
            switch (peek().tokenType){
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }

    private Token consume(TokenType type, String message){
        if (check(type)){
            return advance();
        }
        throw error(peek(), message);
    }

    private boolean match(TokenType...types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type){
        if (isAtEnd()){
            return false;
        }
        return peek().tokenType == type;
    }

    private Token advance(){
        if (!isAtEnd()){
            currenPos++;
        }
        return previous();
    }

    private boolean isAtEnd(){
        return peek().tokenType == EOF;
    }

    private Token peek(){
        return tokens.get(currenPos);
    }

    private Token previous(){
        return tokens.get(currenPos - 1);
    }
}
