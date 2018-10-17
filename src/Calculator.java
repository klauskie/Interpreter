import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class Calculator {

    public int currentTokenPosition = 0;
    public List<Token> tokens;
    public Map<String, Object> symbolTable = new HashMap<String, Object>();

    /*public Calculator(List<Token> tokens){
        super(tokens);
    }*/

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

    public Token NextToken() {
        return GetToken(1);
    }

    // Just eats the token(s) given in the offset
    public void EatToken(int offset)
    {
        currentTokenPosition += offset;
    }

    // Eats the token given type and returns eaten token
    public Token MatchAndEat(TokenType type)
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
        return token;
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
        return Term();
    }

    private Node Divide() {
        MatchAndEat(TokenType.DIVIDE);
        return Term();
    }

    private Node Exponent() {
        MatchAndEat(TokenType.EXPONENT);
        return Factor();
    }

    private Node Modulus() {
        MatchAndEat(TokenType.MODULUS);
        return Term();
    }

    private Node Factor()
    {
        Node result = null;
        if (CurrentToken().type == TokenType.LEFT_PAREN)
        {
            MatchAndEat(TokenType.LEFT_PAREN);
            //result = ArithmeticExpression();
            result = Relation();
            MatchAndEat(TokenType.RIGHT_PAREN);
        }
        else if (CurrentToken().type == TokenType.NUMBER)
        {
            result = new NumberNode(Integer.parseInt(CurrentToken().text));
            MatchAndEat(TokenType.NUMBER);
        }else if (CurrentToken().type == TokenType.WORD)
        {
            Token token = MatchAndEat(TokenType.WORD);
            result = new VariableNode(token.text, this);
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
                CurrentToken().type == TokenType.DIVIDE ||
                CurrentToken().type == TokenType.EXPONENT ||
                CurrentToken().type == TokenType.MODULUS)
        {
            switch(CurrentToken().type)
            {
                case MULTIPLY:
                    result = new BinOpNode(TokenType.MULTIPLY, result, Multiply());
                    break;
                case DIVIDE:
                    result = new BinOpNode(TokenType.DIVIDE, result, Divide());
                    break;
                case EXPONENT:
                    result = new BinOpNode(TokenType.EXPONENT, result, Exponent());
                    break;
                case MODULUS:
                    result = new BinOpNode(TokenType.MODULUS, result, Modulus());
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

    public Node Expression(){
        return Relation();
    }

    // Stuff with Hashmap
    public Object setVariable(String name, Object value) {
        symbolTable.put(name, value);
        return value;
    }

    public Object getVariable(String name) {
        Object value = symbolTable.get(name);
        if (value != null) {
            return value;
        }
        return null;
    }
    // End stuff with Hashmap

    private boolean IsAssignment(TokenType currentType){
        return (currentType == TokenType.WORD && (NextToken().type).equals(TokenType.ASSIGN));
    }

    private boolean IsNewVar(TokenType currentType){
        return (currentType == TokenType.WORD && (NextToken().type).equals(TokenType.NEW_VAR));
    }

    private Node NewVar(){
        String name = MatchAndEat(TokenType.WORD).text;
        MatchAndEat(TokenType.NEW_VAR);
        Node value = Expression();
        return new AssignmentNode(name, value, this);
    }

    private Node Assignmet(){
        String name = MatchAndEat(TokenType.WORD).text;
        MatchAndEat(TokenType.ASSIGN);
        Node value = Expression();

        return new AssignmentNode(name, value, this);
    }

    private Node WhileFunc(){
        Node condition, body;
        MatchAndEat(TokenType.WHILE);
        condition = Expression();
        body = Block();
        return new WhileNode(condition, body);
    }

    public Node Statement(){
        Node nodo = null;
        TokenType type = CurrentToken().type;

        if(IsAssignment(type)){ // If statement is an assignment
            nodo = Assignmet();
        }else if(IsNewVar(type)){ // If statement is a new variable
            nodo = NewVar();
        }else if(type == TokenType.NUMBER){ // If statement starts with a number.
            nodo = Expression();
        }else if(CurrentToken().type == TokenType.WHILE){
            nodo = WhileFunc();
        }
        return nodo;
    }

    public BlockNode Block()
    {
        List<Node> statements = new LinkedList<Node>();
        while ( CurrentToken().type != TokenType.END && CurrentToken().type != TokenType.NEWLINE)
        {
            statements.add(Statement());
        }
        //MatchAndEat(TokenType.END);
        EatToken(1);
        return new BlockNode(statements);
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
