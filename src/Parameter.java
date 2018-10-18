public class Parameter{
    private String name;
    private Node value;

    public Parameter(String name, Node value) {
        this.name = name;
        this.value = value;
    }

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(Node value){
        this.value = value;
    }

    // getValue
    public Object evaluate(){
        return this.value.evaluate();
    }

    public String getName(){
        return name;
    }

    public Object getValue(){
        return this.value.evaluate();
    }


}
