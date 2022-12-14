package wordy.ast;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

/**
 * Wordy’s function return node. It sets the RETURN variable to the context.
 *
 * The interpreter implements this by throwing a `FunctionReturned` exception.
 */
public class FunctionReturnNode extends StatementNode {
    private final ExpressionNode returnValue;

    public FunctionReturnNode(ExpressionNode returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Map.of("return", returnValue);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        FunctionReturnNode functionReturn = (FunctionReturnNode) o;
        return this.returnValue.equals(functionReturn.returnValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnValue);
    }

    @Override
    protected void doRun(EvaluationContext context) {
        context.set("RETURN", returnValue.doEvaluate(context));
        throw new FunctionReturned();
    }

    @Override
    public void compile(PrintWriter out) {
        out.print("return");
        returnValue.compile(out);
    }

    @Override
    public String toString() {
        return "FunctionReturnNode{ value=" + returnValue + "}";
    }
}
