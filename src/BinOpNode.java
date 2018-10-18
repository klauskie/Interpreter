import java.lang.Math;

public class BinOpNode extends Node{

    public TokenType operator;
    public Node left;
    public Node right;

    public BinOpNode(TokenType type, Node left, Node right){
        this.operator = type;
        this.left = left;
        this.right = right;
    }

    public float getValue(Node node) {
        Object res = node.evaluate();
        return (float)res;
    }

    public Object ToObject(Node node)
    {
        return node.evaluate();
    }

    public Object evaluate()
    {
        Object result = null;
        switch(operator)
        {
            case ADD:
                result = getValue(left) + getValue(right);
                break;
            case SUBTRACT:
                result = getValue(left) - getValue(right);
                break;
            case MULTIPLY:
                result = getValue(left) * getValue(right);
                break;
            case DIVIDE:
                if (getValue(right) == 0) {
                    System.out.println("Math Error: Division by Zero!");
                    System.exit(0);
                }
                result = getValue(left) / getValue(right);
                break;
            case EXPONENT:
                result = (int)Math.pow(getValue(left), getValue(right));
                break;
            case MODULUS:
                result = getValue(left) % getValue(right);
                break;
            case LESS_THAN:
                result = getValue(left) < getValue(right);
                break;
            case MORE_THAN:
                result = getValue(left) > getValue(right);
                break;
            case EQUALS:
                result = ToObject(left) == ToObject(right);
                break;
            case NOT_EQUALS:
                result = ToObject(left) != ToObject(right);
                break;
            case LESS_EQUALS:
                result = getValue(left) <= getValue(right);
                break;
            case MORE_EQUALS:
                result = getValue(left) >= getValue(right);
                break;
        }
        return result;
    }

}
