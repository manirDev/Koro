package Interpreter;

import Ast.Expression.*;
import Scanner.Token;
import Error.RuntimeError;

import static Error.RuntimeError.runtimeError;

public class Interpreter implements ExprVisitor<Object> {

    public void interpret(Expr expression){
        try{
            Object value = evaluate(expression);
            System.out.println(stringFy(value));
        }
        catch (RuntimeError error){
            runtimeError(error);
        }
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        switch (expr.operator.tokenType){
            case MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            }
            case SLASH -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            }
            case STAR -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            }
            case PLUS -> {
                if (left instanceof Double && right instanceof Double){
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String){
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expr.operator, "Les opérandes doivent être deux nombres ou deux chaînes de caractères.");
            }
            case GREATER -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            }
            case LESS -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right);
            }
            case EQUAL_EQUAL -> {
                return isEqual(left, right);
            }
        }
        return null;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.tokenType){
            case BANG -> {
                return !isTruthy(right);
            }
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            }
        }
        return null;
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    private boolean isTruthy(Object object){
        if (object == null){
            return false;
        }
        if (object instanceof Boolean){
            return (boolean)object;
        }
        return true;
    }

    private boolean isEqual(Object left, Object right){
        if (left == null && right == null){
            return true;
        }
        if (left == null){
            return false;
        }
        return left.equals(right);
    }

    private void checkNumberOperand(Token operator, Object operand){
        if (operand instanceof Double){
            return;
        }
        throw new RuntimeError(operator, "L'opérande doit être un nombre");
    }

    private void checkNumberOperands(Token operator, Object left, Object right){
        if (left instanceof Double && right instanceof Double){
            return;
        }
        throw new RuntimeError(operator, "Les opérandes doivent être des nombres");
    }

    private String stringFy(Object object){
        if (object == null){
            return "nul";
        }
        if (object instanceof Double){
            String res = object.toString();
            if (res.endsWith(".0")){
                res = res.substring(0, res.length() - 2);
            }
            return res;
        }
        return object.toString();
    }
}
