package Environment;

import Interpreter.Interpreter;

import java.util.List;

public interface KoroCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
