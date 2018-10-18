
public class AssignmentNode extends Node
{
    public String name;
    public Node value;
    public Calculator parser;

    public AssignmentNode(String name, Node value, Calculator parser) {
        this.name = name;
        this.value = value;
        this.parser = parser;

    }
    public Object evaluate()
    {
        if (value instanceof FunctionNode){
            return parser.setVariable(name, value);
        }else if(value instanceof FunctionCallNode){
            return parser.setVariable(name, value);
        }
        else{
            return parser.setVariable(name, value.evaluate());
        }

    }
}