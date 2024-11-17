package Ast.Expression;

public interface  ExprVisitor<T> {
    T visitBinaryExpr(Binary expr);
    T visitUnaryExpr(Unary expr);
    T visitLiteralExpr(Literal expr);
    T visitGroupingExpr(Grouping expr);
    T visitVariableExpr(Variable expr);
    T visitAssignExpr(Assign expr);
    T visitLogicalExpr(Logical expr);
    T visitCallExpr(Call expr);
}
