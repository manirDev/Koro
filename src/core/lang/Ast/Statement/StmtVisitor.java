package Ast.Statement;

public interface StmtVisitor <T>{
    T visitExpressionStmt(Expression stmt);
    T visitPrintStmt(Print stmt);
    T visitVarStmt(Var stmt);
    T visitBlockStmt(Block stmt);
    T visitIfStmt(If stmt);
    T visitWhileStmt(While stmt);
}
