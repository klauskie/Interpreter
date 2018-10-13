public class NegOpNode extends Node{
    public Node node;

    public NegOpNode(Node node){
        this.node = node;
    }

    public int ToInt(Node node){
        Object result = node.evaluate();
        return (Integer)result;
    }

    public Object evaluate() {
        return -ToInt(this.node);
    }
}
