package Ast.Statement;

import Ast.Expression.Expr;
import Scanner.Token;

public class Var extends Stmt{
    public final Token name;
    public final Expr initializer;
    public Var(Token name, Expr initializer){
        this.name = name;
        this.initializer = initializer;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitVarStmt(this);
    }
}
