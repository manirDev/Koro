package Ast.Expression;

public class Literal extends Expr{
    public final Object value;
    public Literal(Object value){
        this.value = value;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}
