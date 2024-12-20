package Ast.Expression;

import Scanner.Token;

import java.util.List;

public class Call extends Expr{
    public final Expr callee;
    public final Token paren;
    public final List<Expr> arguments;

    public Call(Expr callee, Token paren, List<Expr> arguments){
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }
    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitCallExpr(this);
    }
}
