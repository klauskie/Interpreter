public class VariableNode extends Node {
    public String name;
    public Calculator calculator;

    public VariableNode(String name, Calculator calculator){
        this.name = name;
        this.calculator = calculator;
    }

    public Object evaluate(){
        Object varValue = this.calculator.getVariable(this.name);
        if (varValue == null) {
            System.out.println("Undefined Variable Name: " + this.name);
            System.exit(1);
        }
        return varValue;
    }
}
