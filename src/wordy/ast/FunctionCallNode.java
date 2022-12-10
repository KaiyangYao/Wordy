package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return 0;
    }

    @Override
    protected double doEvaluate(EvaluationContext context) {
        FunctionNode function = context.getFunction(name.getName());
        EvaluationContext closureContext = new EvaluationContext();
        for (ExpressionNode arg : args) {
//            String varName = function.getParams().getName();
//            closureContext.set(varName, context.get(varName));
        }
        function.getBody().doRun(closureContext);
        return 0;
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
