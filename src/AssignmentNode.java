public class AssignmentNode extends Node
{
    public String name;
    public Node value;
    public Calculator parser;
    public String scope;

    public AssignmentNode(String name, Node value, Calculator parser)
    {
        this.name = name;
        this.value = value;
        this.parser = parser;
    }
    public Object evaluate()
    {
        return parser.setVariable(name, value.evaluate());
    }
}