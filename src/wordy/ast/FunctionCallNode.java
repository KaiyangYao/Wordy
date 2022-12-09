package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FunctionCallNode extends ExpressionNode {
    private final VariableNode name;
    private final List<VariableNode> params;

    public FunctionCallNode(VariableNode name, List<VariableNode> parameters) {
        this.name = name;
        this.params = parameters;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Collections.emptyMap();
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
        return 0;
    }

    @Override
    public String toString() {
        return "FunctionCallNode{name=" + name + " params=" + params.toString() + "}";
    }
}
