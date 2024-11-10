package Ast.Statement;

import java.util.List;

public class Block extends Stmt{
    public final List<Stmt> statements;
    public Block(List<Stmt> statements){
        this.statements = statements;
    }
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitBlockStmt(this);
    }
}
