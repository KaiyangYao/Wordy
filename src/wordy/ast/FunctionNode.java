package wordy.ast;

import wordy.interpreter.EvaluationContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FunctionNode extends StatementNode {
    private final String functionName;
    private final ArrayList<VariableNode> parameters;
    private final StatementNode body;
    private double returnValue;

    public FunctionNode(String functionName, ArrayList<VariableNode> parameters, StatementNode body) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Map.of("body", body);
    }

    @Override
    public void doRun(EvaluationContext context) {
        body.run(context); // I have no idea if this is enough
    }

    @Override
    public void compile(PrintWriter out) {
        out.println("while(true)"); // This is unchanged from the LoopNode, which I'm basing it off of, I'm 95% sure we don't need it
        body.compile(out);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        FunctionNode fnNode = (FunctionNode) o;
        return body.equals(fnNode.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }

    @Override
    public String toString() {
        return "FunctionNode{body=" + body + '}';
    }
    
}
