import java.util.List;

public class FunctionNode extends Node
{
    private Node body;
    private List<Parameter> parameters;
    private String name;

    public FunctionNode(String name, Node body, List<Parameter> parameters)
    {
        this.body = body;
        this.parameters = parameters;
        this.name = name;
    }
    public Object evaluate()
    {
        return body.evaluate();
    }
    public List<Parameter> getParameters()
    {
        return parameters;
    }
    public Node getBody()
    {
        return body;
    }

    public String getName()
    {
        return name;
    }
}