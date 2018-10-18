public class ForNode extends Node {

    private Node variable;
    private Node condition;
    private Node var_increment;
    private Node body;

    public ForNode(Node variable, Node condition, Node var_increment, Node body) {
        this.variable = variable;
        this.condition = condition;
        this.var_increment = var_increment;
        this.body = body;
    }

    @Override
    public Object evaluate(){
        Object temp = null;

        for(variable.evaluate(); (boolean)condition.evaluate(); var_increment.evaluate()){
            temp = this.body.evaluate();
        }
        return temp;
    }

}
