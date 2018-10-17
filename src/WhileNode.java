public class WhileNode extends Node {

    public Node condition;
    public Node body;

    public WhileNode(Node condition, Node body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Object evaluate() {
        Object temp = null;
        while((boolean)condition.evaluate()){
            temp = body.evaluate();
        }
        return temp;
    }
}
