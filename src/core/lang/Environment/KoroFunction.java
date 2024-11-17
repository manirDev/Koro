package Environment;

import Ast.Statement.Function;
import Ast.Statement.Stmt;
import Interpreter.Interpreter;
import exception.ReturnVal;

import java.util.List;

public class KoroFunction implements KoroCallable{
    private final Function declaration;
    private final Environment closure;

    public KoroFunction(Function declaration, Environment closure){
        this.declaration = declaration;
        this.closure = closure;
    }
    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i=0; i<declaration.params.size(); i++){
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }
        try{
            interpreter.executeBlock(declaration.body, environment);
        }
        catch (ReturnVal returnVal){
            return returnVal.value;
        }
        return null;
    }

    @Override
    public String toString() {
        return "<fon " + declaration.name.lexeme + ">";
    }
}
