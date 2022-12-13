package wordy.ast;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;

import java.io.PrintWriter;
import java.util.*;

/**
 * Wordy’s function call node, which finds the targeted function in the context and executes it.
 * It terminates the function by catching a `FunctionReturned` exception.
 *
 * Recursion is NOT allowed because the implementation doesn't support lexical capture / access local scope in function.
 */
public class FunctionCallNode extends ExpressionNode {
    private final VariableNode name;
    private final List<ExpressionNode> args;

    public FunctionCallNode(VariableNode name, List<ExpressionNode> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> arguments = new LinkedHashMap<>();
        for (int i = 0; i < args.size(); i++) {
            arguments.put("arg" + (i + 1), args.get(i));
        }
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        FunctionCallNode functionCall = (FunctionCallNode) o;
        return this.name.getName().equals(functionCall.name.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    protected double doEvaluate(EvaluationContext context) {
        FunctionNode function = context.getFunction(name.getName());
        List<VariableNode> params = function.getParams();
        EvaluationContext closureContext = new EvaluationContext();
        double returnVal = 0.0;

        if (params.size() != args.size()) {
            throw new RuntimeException("Incorrect number of arguments");
        }

        try {
            for (int i = 0; i < params.size(); i++) {
                closureContext.set(params.get(i).getName(), args.get(i).evaluate(context));
            }
            closureContext.setFunction(name.getName(), function);
            function.getBody().doRun(closureContext);
        } catch (FunctionReturned e) {
            returnVal = closureContext.get("RETURN");
        }

        return returnVal;
    }

    @Override
    public void compile(PrintWriter out) {
        name.compile(out);
        out.print(" (");
        for (int i = 0; i < args.size() - 1; i++) {
            args.get(i).compile(out);
            out.print(", ");
        }
        args.get(args.size()-1).compile(out);
        out.print(");");
    }

    @Override
    public String toString() {
        return "FunctionCallNode{name=" + name + " params=" + args.toString() + "}";
    }

    @Override
    protected String describeAttributes() {
        return "(function name=\"" + name.getName() + "\")";
    }
}
