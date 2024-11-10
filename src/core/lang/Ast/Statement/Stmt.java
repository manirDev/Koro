package Ast.Statement;

public abstract class Stmt {
    public abstract <T> T accept(StmtVisitor<T> visitor);
}
