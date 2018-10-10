public class NumberNode extends Node {

    public Integer value;

    public NumberNode(Integer type){
        this.value = type;
    }
    public Object evaluate() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
