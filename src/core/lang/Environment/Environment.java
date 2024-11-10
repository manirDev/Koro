package Environment;

import Scanner.Token;

import java.util.HashMap;
import java.util.Map;
import Error.RuntimeError;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    public void define(String name, Object value){
        values.put(name, value);
    }

    public Object get(Token name){
        if (values.containsKey(name.lexeme)){
            return values.get(name.lexeme);
        }
        throw new RuntimeError(name, "Variable non définie '" + name.lexeme + "'.");
    }


}
