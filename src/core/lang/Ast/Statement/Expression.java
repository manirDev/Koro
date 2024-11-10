package Ast.Statement;

import Ast.Expression.Expr;

public class Expression extends Stmt{
    public final Expr expression;
    public Expression(Expr expression){
        this.expression = expression;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
