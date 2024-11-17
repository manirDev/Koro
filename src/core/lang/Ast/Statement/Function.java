package Ast.Statement;

import Scanner.Token;

import java.util.List;

public class Function extends Stmt{
    public final Token name;
    public final List<Token> params;
    public final List<Stmt> body;

    public Function(Token name, List<Token> params, List<Stmt> body){
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitFunctionStmt(this);
    }
}
