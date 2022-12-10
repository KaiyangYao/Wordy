package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.util.Collections;
import java.util.Map;

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
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    protected void doRun(EvaluationContext context) {
        context.set("RETURN", returnValue.doEvaluate(context));
    }

    @Override
    public String toString() {
        return "FunctionReturnNode{ value=" + returnValue + "}";
    }
}
