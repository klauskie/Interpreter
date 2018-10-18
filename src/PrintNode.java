public class PrintNode extends Node {

    private Node stuff;

    public PrintNode(Node stuff){
        this.stuff = stuff;
    }

    public Object evaluate(){
        return stuff.evaluate();
    }

}
