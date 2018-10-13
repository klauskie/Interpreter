import java.util.List;

public class Calculator extends Parser{

    public int currentTokenPosition = 0;
    //public List<Token> tokens;

    public Calculator(List<Token> tokens){
        super(tokens);
    }

    public Token GetToken(int offset)
    {
        if (currentTokenPosition + offset >= tokens.size())
        {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(currentTokenPosition + offset);
    }

    public Token CurrentToken()
    {
        return GetToken(0);
    }

    // Just eats the token(s) given in the offset
    public void EatToken(int offset)
    {
        currentTokenPosition += offset;
    }

    // Eats the token given type and returns eaten token
    public void MatchAndEat(TokenType type)
    {
        Token token = CurrentToken();
        if (CurrentToken().type != type)
        {
            System.out.println("Saw " + token.type +
                    " but " + type +
                    " expected");
            System.exit(0);
        }
        EatToken(1);
    }

    private Node Add() {
        MatchAndEat(TokenType.ADD); // podria llamar solamente a EatToken
        return Term();
    }

    private Node Subtract() {
        MatchAndEat(TokenType.SUBTRACT);
        return Term();
    }

    private Node Multiply() {
        MatchAndEat(TokenType.MULTIPLY);
        return Factor();
    }

    private Node Divide() {
        MatchAndEat(TokenType.DIVIDE);
        return Factor();
    }

    private Node Factor()
    {
        Node result = null;
        if (CurrentToken().type == TokenType.LEFT_PAREN)
        {
            MatchAndEat(TokenType.LEFT_PAREN);
            result = ArithmeticExpression();
            MatchAndEat(TokenType.RIGHT_PAREN);
        }
        else if (CurrentToken().type == TokenType.NUMBER)
        {
            result = new NumberNode(Integer.parseInt(CurrentToken().text));
            MatchAndEat(TokenType.NUMBER);
        }
        return result;
    }

    private Node SignedFactor(){
        if(CurrentToken().type == TokenType.SUBTRACT){
            MatchAndEat(TokenType.SUBTRACT);
            return new NegOpNode(Factor());
        }
        return Factor();
    }

    public Node Term()
    {
        Node result = SignedFactor();
        while ( CurrentToken().type == TokenType.MULTIPLY ||
                CurrentToken().type == TokenType.DIVIDE )
        {
            switch(CurrentToken().type)
            {
                case MULTIPLY:
                    result = new BinOpNode(TokenType.MULTIPLY, result, Multiply());
                    break;
                case DIVIDE:
                    result = new BinOpNode(TokenType.DIVIDE, result, Divide());
                    break;
            }
        }
        return result;
    }

    public Node ArithmeticExpression()
    {
        Node result = Term();
        while (CurrentToken().type == TokenType.ADD ||
                CurrentToken().type == TokenType.SUBTRACT)
        {
            switch(CurrentToken().type)
            {
                case ADD:
                    result = new BinOpNode(TokenType.ADD, result, Add());
                    break;
                case SUBTRACT:
                    result = new BinOpNode(TokenType.SUBTRACT, result, Subtract());
                    break;
            }
        }
        return result;
    }

    public Node Relation(){
        Node result = ArithmeticExpression();
        TokenType current = CurrentToken().type;
        if(current == TokenType.LESS_THAN ||
                current == TokenType.MORE_THAN ||
                current == TokenType.LESS_EQUALS ||
                current == TokenType.MORE_EQUALS ||
                current == TokenType.EQUALS ||
                current == TokenType.NOT_EQUALS)
        {
            EatToken(1);
            switch (current){
                case LESS_THAN:
                    result = new BinOpNode(TokenType.LESS_THAN, result, ArithmeticExpression());
                    break;
                case MORE_THAN:
                    result = new BinOpNode(TokenType.MORE_THAN, result, ArithmeticExpression());
                    break;
                case LESS_EQUALS:
                    result = new BinOpNode(TokenType.LESS_EQUALS, result, ArithmeticExpression());
                    break;
                case MORE_EQUALS:
                    result = new BinOpNode(TokenType.MORE_EQUALS, result, ArithmeticExpression());
                    break;
                case EQUALS:
                    result = new BinOpNode(TokenType.EQUALS, result, ArithmeticExpression());
                    break;
                case NOT_EQUALS:
                    result = new BinOpNode(TokenType.NOT_EQUALS, result, ArithmeticExpression());
                    break;

            }
        }

        return result;

    }

    public Node Expresion(){
        return Relation();
    }

    public void PrettyPrint(List<Token> tokens)
    {
        int numberCount = 0;
        int opCount = 0;
        for (Token token: tokens)
        {

            if (token.type == TokenType.NUMBER)
            {
                System.out.println("Number....: " + token.text);
                numberCount++;
            }
            else if (token.type == TokenType.WORD) {
                System.out.println("WORD..: " + token.text);
            }
            else
            {
                System.out.println("Operator..: " + token.type);
                opCount++;
            }

        }
        System.out.println("You have got "+ numberCount + " different number and " + opCount + " operators.");
    }
}
