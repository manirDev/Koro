package Ast.Statement;

import Ast.Expression.Expr;

public class Print extends Stmt{
    public final Expr expression;
    public Print(Expr expression){
        this.expression = expression;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
