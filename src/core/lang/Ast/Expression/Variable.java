package Ast.Expression;

import Scanner.Token;

public class Variable extends Expr{
    public final Token name;
    public Variable(Token name){
        this.name = name;
    }
    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
