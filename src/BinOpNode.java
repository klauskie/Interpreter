public class BinOpNode extends Node{

    public TokenType operator;
    public Node left;
    public Node right;

    public BinOpNode(TokenType type, Node left, Node right){
        this.operator = type;
        this.left = left;
        this.right = right;
    }

    public int getValue(Node node)
    {
        Object res = node.evaluate();
        return (Integer)res;
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

    public static void main(String[] args){
        NumberNode firstNumber = new NumberNode(100);
        NumberNode secondNumber = new NumberNode(100);
        BoolNode first = new BoolNode(true);
        BoolNode second = new BoolNode(true);
        Node sumNode = new BinOpNode(TokenType.ADD, firstNumber, secondNumber);
        System.out.println("100 + 100 = " + sumNode.evaluate());
        Node compareNode = new BinOpNode(TokenType.EQUALS, firstNumber, secondNumber);
        System.out.println("100 == 100 = " + compareNode.evaluate());
    }
}
