public class BoolNode extends Node {
    public Boolean value;

    public BoolNode(Boolean type){
        this.value = type;
    }

    public Object evaluate(){
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
