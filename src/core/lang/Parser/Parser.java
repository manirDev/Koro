package Parser;

import Ast.Expression.*;
import Ast.Statement.*;
import Scanner.Token;
import Scanner.TokenType;

import java.sql.Array;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Error.ParseError;
import com.sun.nio.file.ExtendedOpenOption;

import static Error.ParseError.error;
import static Scanner.TokenType.*;
import static Utils.CONSTANT.ARG_SIZE;

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
        return assignment();
    }

    private Stmt declaration(){
        try{
            if (match(FUN)){
                return function("function");
            }
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

    private Function function(String kind){
        Token name = consume(IDENTIFIER, "Attendu " + kind + " nom.");
        consume(OPEN_PAREN, "Attendu '(' apres " + kind + " nom.");
        List<Token> parameters = new ArrayList<>();
        if (!check(CLOSE_PAREN)){
            do {
                if (parameters.size() >= ARG_SIZE){
                    error(peek(), "Impossible d'avoir plus de " + ARG_SIZE + " paramètres.");
                }
                parameters.add(consume(IDENTIFIER, "Attendu le nom du parametre"));
            }while (match(COMMA));
        }
        consume(CLOSE_PAREN, "Attendu ')' apres les parametres");

        consume(OPEN_BRACE, "Attendu '(' apres " + kind + " corps.");
        List<Stmt> body = block();
        return new Function(name, parameters, body);
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
        if (match(FOR)){
            return forStatement();
        }
        if (match(IF)){
            return ifStatement();
        }
        if (match(PRINT)){
            return printStatement();
        }
        if (match(RETURN)){
            return returnStatement();
        }
        if (match(WHILE)){
            return whileStatement();
        }
        if (match(OPEN_BRACE)){
            return new Block(block());
        }
        return expressionStatement();
    }

    private Stmt returnStatement(){
        Token keyword = previous();
        Expr value = null;
        if (!check(SEMICOLON)){
            value = expression();
        }
        consume(SEMICOLON, "Attendu ';' apres une valeur retournee");
        return new Return(keyword, value);
    }

    private Stmt forStatement(){
        consume(OPEN_PAREN, "Attendu '(' apres une 'Pour' boucle");
        Stmt initializer;
        if (match(SEMICOLON)){
            initializer = null;
        }
        else if(match(VAR)){
            initializer = varDeclaration();
        }
        else {
            initializer = expressionStatement();
        }
        Expr condition = null;
        if (!check(SEMICOLON)){
            condition = expression();
        }
        consume(SEMICOLON, "Attendu ';' apres une condition.");
        Expr increment = null;
        if (!check(CLOSE_PAREN)){
            increment = expression();
        }
        consume(CLOSE_PAREN, "Attendu ')' apres une boucle 'Pour'.");
        Stmt body = statement();

        if (increment != null){
            body = new Block(
                    Arrays.asList(
                            body,
                            new Expression(increment)
                    )
            );
        }
        if (condition == null){
            condition = new Literal(true);
        }
        body = new While(condition, body);

        if (initializer != null){
            body = new Block(Arrays.asList(initializer, body));
        }

        return body;
    }
    private Stmt ifStatement(){
        consume(OPEN_PAREN, "Attendu '(' apres une 'Si'");
        Expr condition = expression();
        consume(CLOSE_PAREN, "Attendu ')' apres une 'Si' condition");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(ELSE)){
            elseBranch = statement();
        }
        return new If(condition, thenBranch, elseBranch);
    }

    private Stmt printStatement(){
        Expr value = expression();
        consume(SEMICOLON, "Attendu ';' apres une valeur");
        return new Print(value);
    }

    private Stmt whileStatement(){
        consume(OPEN_PAREN, "Attendu '(' apres une 'Tant_que'.");
        Expr condition = expression();
        consume(CLOSE_PAREN, "Attendu ')' apres une boucle 'Tant_que'");
        Stmt body = statement();
        return new While(condition, body);
    }

    private Stmt expressionStatement(){
        Expr expr = expression();
        consume(SEMICOLON, "Attendu ';' apres une expression");
        return new Expression(expr);
    }

    private List<Stmt> block(){
        List<Stmt> statements = new ArrayList<>();
        while (!check(CLOSE_BRACE) && !isAtEnd()){
            statements.add(declaration());
        }
        consume(CLOSE_BRACE, "Un '}' est attendu après le bloc.");
        return statements;
    }

    private Expr assignment(){
        Expr expr = or();
        if (match(EQUAL)){
            Token equals = previous();
            Expr value = assignment();
            if (expr instanceof Variable){
                Token name = ((Variable)expr).name;
                return new Assign(name, value);
            }
            error(equals, "Cible d'affectation invalide.");
        }
        return expr;
    }

    private Expr or(){
        Expr expr = and();
        while (match(OR)){
            Token operator = previous();
            Expr right = and();
            expr = new Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr and(){
        Expr expr = equality();
        while (match(AND)){
            Token operator = previous();
            Expr right = equality();
            expr = new Logical(expr, operator, right);
        }
        return expr;
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
        return call();
    }

    private Expr call(){
        Expr expr = primary();
        while (true){
            if (match(OPEN_PAREN)){
                expr = finishCall(expr);
            }
            else{
                break;
            }
        }
        return expr;
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

    private Expr finishCall(Expr callee){
        List<Expr> arguments = new ArrayList<>();
        if (!check(CLOSE_PAREN)){
            do{
                if(arguments.size() >= ARG_SIZE){
                    error(peek(), "Pas plus de " + ARG_SIZE + " arguments");
                }
                arguments.add(expression());
            }while (match(COMMA));
        }

        Token paren = consume(CLOSE_PAREN, "Attendu ')' apres les arguments");
        return new Call(callee, paren, arguments);
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
