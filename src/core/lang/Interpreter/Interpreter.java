package Interpreter;

import Ast.AstPrinter;
import Ast.Expression.*;
import Ast.Statement.*;
import Environment.Environment;
import Environment.KoroCallable;
import Environment.KoroFunction;
import Scanner.Token;
import Error.RuntimeError;
import Scanner.TokenType;
import exception.ReturnVal;

import java.util.ArrayList;
import java.util.List;

import static Error.RuntimeError.runtimeError;
import static Utils.CONSTANT.*;

public class Interpreter implements ExprVisitor<Object>, StmtVisitor<Void> {
    private final Environment globals = new Environment();
    private Environment environment = globals;

    public Interpreter(){
        globals.define("clock", new KoroCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double)System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fon>";
            }
        });
    }

    public void interpret(List<Stmt> statements){
        try{
            for (Stmt statement : statements){
                execute(statement);
            }
        }
        catch (RuntimeError error){
            runtimeError(error);
        }
    }

    @Override
    public Void visitVarStmt(Var stmt) {
        Object value = null;
        if (stmt.initializer != null){
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitBlockStmt(Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStmt(If stmt) {
        AstPrinter printer = new AstPrinter();
        //System.out.println(printer.print(stmt.condition));
        if (isTruthy(evaluate(stmt.condition))){
            execute(stmt.thenBranch);
        }
        else if (stmt.elseBranch != null){
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(While stmt) {
        while (isTruthy(evaluate(stmt.condition))){
            execute(stmt.body);
        }
        return  null;
    }

    @Override
    public Void visitFunctionStmt(Function stmt) {
        KoroFunction function = new KoroFunction(stmt, environment);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitReturnStmt(Return stmt) {
        Object value = null;
        if (stmt.value != null){
            value = evaluate(stmt.value);
        }
        throw new ReturnVal(value);
    }

    public void executeBlock(List<Stmt> statements, Environment environment){
        Environment previous = this.environment;
        try{
            this.environment = environment;
            for (Stmt statement : statements){
                execute(statement);
            }
        }
        finally {
            this.environment = previous;
        }
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        if (value instanceof String) {
            System.out.print(stringFy(value));
        } else {
            System.out.print(stringFy(value));
        }
        return null;
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
                    return left + (String)right;
                }
                if (left instanceof String || right instanceof String){
                    return stringFy(left).concat(stringFy(right));
                }
                throw new RuntimeError(expr.operator, "Les opérandes doivent être deux nombres ou deux chaînes de caractères.");
            }
            case GREATER -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right ? K_TRUE : K_FALSE;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right ? K_TRUE : K_FALSE;
            }
            case LESS -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right ? K_TRUE : K_FALSE;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right ? K_TRUE : K_FALSE;
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right) ? K_TRUE : K_FALSE;
            }
            case EQUAL_EQUAL -> {
                return isEqual(left, right) ? K_TRUE : K_FALSE;
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
        if (expr.value instanceof Boolean){
            return (boolean)expr.value ? K_TRUE : K_FALSE;
        }
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visitAssignExpr(Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        Object left = evaluate(expr.left);
        if (expr.operator.tokenType == TokenType.OR){
            if (isTruthy(left)){
                return left;
            }
        }
        else {
            if (!isTruthy(left)){
                return left;
            }
        }
        return evaluate(expr.right);
    }

    @Override
    public Object visitCallExpr(Call expr) {
        Object callee = evaluate(expr.callee);
        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments){
            arguments.add(evaluate(argument));
        }
        if (!(callee instanceof KoroCallable)){
            throw new RuntimeError(expr.paren,
                    "On peut seulement appeler des fonctions et des classes.");
        }
        KoroCallable function = (KoroCallable)callee;
        if (arguments.size() != function.arity()){
            throw new RuntimeError(
                            expr.paren,
                    "Attendu " +
                            function.arity() + " arguments, mais reçu " +
                            arguments.size() + "."
                            );
        }
        return function.call(this, arguments);
    }

    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    private void execute(Stmt stmt){
        stmt.accept(this);
    }

    private boolean isTruthy(Object object){
        if (object == null){
            return false;
        }
        if (object instanceof String){
            String value = (String)object;
            if (value.equals("vrai")){
                return true;
            }
            return false;
        }
        if (object instanceof Boolean){
            return (boolean)object;
        }
        /*if (object instanceof String){
            return !((String)object).isEmpty();
        }
        if (object instanceof Number){
            Number numObj = (Number)object;
            if (numObj.longValue() == 0 && numObj.doubleValue() != 0){
                return true;
            }
            return numObj.longValue() != 0;
        }*/
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
