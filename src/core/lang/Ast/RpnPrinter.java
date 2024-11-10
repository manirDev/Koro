package Ast;

import Ast.Expression.*;
import Scanner.Token;
import Scanner.TokenType;

public class RpnPrinter implements ExprVisitor<String> {
    public String print(Expr expr){
        return expr.accept(this);
    }
    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null){
            return "nul";
        }
        return expr.value.toString();
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return null;
    }

    private String parenthesize(String name, Expr...exprs){
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Expr expr : exprs){
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")").append(name);
        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Binary(
                new Binary(
                        new Literal(1),
                        new Token(TokenType.PLUS, "+", null, 1),
                        new Literal(2)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Binary(
                        new Literal(4),
                        new Token(TokenType.PLUS, "-", null, 1),
                        new Literal(null)
                )
        );
        System.out.println(new RpnPrinter().print(expression));
    }
}
