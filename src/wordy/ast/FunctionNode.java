package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Wordy’s function declaration node, which stores the current function into local context when being executed
 */
public class FunctionNode extends StatementNode {
    private final VariableNode name;
    private final List<VariableNode> params;
    private final StatementNode body;

    public FunctionNode(VariableNode name, List<VariableNode> parameters, StatementNode body) {
        this.name = name;
        this.params = parameters;
        this.body = body;
    }

    public StatementNode getBody() {
        return body;
    }

    public List<VariableNode> getParams() {
        return params;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return body == null ? Collections.emptyMap(): Map.of("body", body);
    }

    @Override
    public void doRun(EvaluationContext context) {
        context.setFunction(name.getName(), this);
    }

    @Override
    public void compile(PrintWriter out) {
        out.print("public static double ");
        name.compile(out);
        out.print(" (");
        for (int i = 0; i < params.size() - 1; i++) {
            params.get(i).compile(out);
            out.print(", ");
        }
        params.get(params.size()-1).compile(out);
        out.println(" ) {");
        body.compile(out);
        out.println(" }");
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        FunctionNode fnNode = (FunctionNode) o;
        return name.equals(fnNode.name)
            && params.equals(fnNode.params)
            && ((body == null && fnNode.body == null) || (body.equals(fnNode.body)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }

    @Override
    public String toString() {
        return "FunctionNode{name=" + name + " params=" + params.toString() + " body=" + body + "}";
    }
    
}
