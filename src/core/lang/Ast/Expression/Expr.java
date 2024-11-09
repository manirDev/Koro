package Ast.Expression;

public abstract class Expr {
    public abstract <T> T accept(ExprVisitor<T> visitor);
}
