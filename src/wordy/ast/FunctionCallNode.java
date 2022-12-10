package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FunctionCallNode extends ExpressionNode {
    private final VariableNode name;
    private final List<ExpressionNode> args;

    public FunctionCallNode(VariableNode name, List<ExpressionNode> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> arguments = new HashMap<>();
        for (int i = 0; i < args.size(); i++) {
            arguments.put("arg" + (i + 1), args.get(i));
        }
        return arguments;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
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
        if (params.size() != args.size()) {
            throw new RuntimeException("Incorrect number of arguments");
        }
        for (int i = 0; i < params.size(); i++) {
            closureContext.set(params.get(i).getName(), args.get(i).evaluate(context));
        }

        function.getBody().doRun(closureContext);

        return closureContext.get("RETURN");
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
