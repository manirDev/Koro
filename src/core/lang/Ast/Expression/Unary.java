package Ast.Expression;

import Scanner.Token;

public class Unary extends Expr{
    public final Token operator;
    public final Expr right;
    public Unary(Token operator, Expr right){
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
