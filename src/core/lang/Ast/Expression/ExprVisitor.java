package Ast.Expression;

public interface  ExprVisitor<T> {
    T visitBinaryExpr(Binary expr);
    T visitUnaryExpr(Unary expr);
    T visitLiteralExpr(Literal expr);
    T visitGroupingExpr(Grouping expr);
}