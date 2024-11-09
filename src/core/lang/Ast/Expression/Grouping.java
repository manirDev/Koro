package Ast.Expression;

public class Grouping extends Expr{
    public final Expr expression;
    public Grouping(Expr expression){
        this.expression = expression;
    }
    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }
}
