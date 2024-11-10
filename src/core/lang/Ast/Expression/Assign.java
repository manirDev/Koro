package Ast.Expression;

import Scanner.Token;

public class Assign extends Expr{
    public final Token name;
    public final Expr value;
    public Assign(Token name, Expr value){
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitAssignExpr(this);
    }
}
