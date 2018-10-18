public class NumberNode extends Node {

    public Float value;

    public NumberNode(Float type){
        this.value = type;
    }
    public Object evaluate() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
