public class IfNode extends Node
{
    public Node condition;
    public Node thenPart;
    public Node elsePart;

    public IfNode(Node condition, Node thenPart, Node elsePart)
    {
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }
    @Override
    public Object evaluate(){
        Object temp = null;

        if((condition != null) && (thenPart != null)){
            if((boolean)condition.evaluate()){
                temp = thenPart.evaluate();
            }
        }
        if((condition != null) && (elsePart != null)){
            if((boolean)condition.evaluate()){
                temp = elsePart.evaluate();
            }
        }

        return temp;

    }
}