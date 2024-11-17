package Ast.Statement;

import Ast.Expression.Expr;
import Scanner.Token;

public class Return extends Stmt{
    public final Token keyword;
    public final Expr value;

    public Return(Token keyword, Expr value){
        this.keyword = keyword;
        this.value = value;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
