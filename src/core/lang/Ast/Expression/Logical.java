package Ast.Expression;

import Scanner.Token;

public class Logical extends Expr{
    public final Expr left;
    public final Token operator;
    public final Expr right;

    public Logical(Expr left, Token operator, Expr right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitLogicalExpr(this);
    }
}
