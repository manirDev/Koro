package Ast.Statement;

import Ast.Expression.Expr;

public class While extends Stmt{
    public final Expr condition;
    public final Stmt body;

    public While(Expr condition, Stmt body){
        this.condition = condition;
        this.body = body;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitWhileStmt(this);
    }
}
