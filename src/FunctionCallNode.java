import java.util.List;
import java.util.ArrayList;

public class FunctionCallNode extends Node {

    public Node name;
    public List<Parameter> actualParameters;
    public Calculator parser;

    public FunctionCallNode(Node name, List<Parameter> actualParameters, Calculator parser) {
        this.name = name;
        this.actualParameters = actualParameters;
        this.parser = parser;
    }

    @Override
    public Object evaluate(){
        FunctionNode function = (FunctionNode) this.name.evaluate();

        return null;
    }
}