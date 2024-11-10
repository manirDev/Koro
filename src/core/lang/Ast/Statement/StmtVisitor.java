package Ast.Statement;

public interface StmtVisitor <T>{
    T visitExpressionStmt(Expression stmt);
    T visitPrintStmt(Print stmt);
}
