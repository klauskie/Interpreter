import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Calculator {

    private int currentTokenPosition = 0;
    private List<Token> tokens;
    public Map<String, Object> stack = new HashMap<String, Object>();

    // ---- Constructor----
    public Calculator(List<Token> tokens){
        this.tokens = tokens;
        this.setVariable("PI", 3.141592653589793f);
        this.setVariable("E", 2.718281828459045f);
    }

    private Token GetToken(int offset) {
        if (currentTokenPosition + offset >= tokens.size())
        {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(currentTokenPosition + offset);
    }

    private Token CurrentToken()
    {
        return GetToken(0);
    }

    private Token NextToken() {
        return GetToken(1);
    }

    private void EatToken(int offset)
    {
        currentTokenPosition += offset;
    }

    private Token MatchAndEat(TokenType type) {
        Token token = CurrentToken();
        if (CurrentToken().type != type) {
            System.out.println("Saw " + token.type + " but " + type + " expected");
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

    private Node Factor() {

        Node result = null;
        if (CurrentToken().type == TokenType.LEFT_PAREN) {

            MatchAndEat(TokenType.LEFT_PAREN);
            result = Relation();
            MatchAndEat(TokenType.RIGHT_PAREN);
        }
        else if (CurrentToken().type == TokenType.NUMBER) {

            //result = new NumberNode(Integer.parseInt(CurrentToken().text));
            result = new NumberNode(Float.parseFloat(CurrentToken().text));
            MatchAndEat(TokenType.NUMBER);
        }
        else if (CurrentToken().type == TokenType.WORD) {

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

    private Node Term() {

        Node result = SignedFactor();
        while ( CurrentToken().type == TokenType.MULTIPLY ||
                CurrentToken().type == TokenType.DIVIDE ||
                CurrentToken().type == TokenType.EXPONENT ||
                CurrentToken().type == TokenType.MODULUS)
        {
            switch(CurrentToken().type) {

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

    private Node ArithmeticExpression() {

        Node result = Term();
        while (CurrentToken().type == TokenType.ADD ||
                CurrentToken().type == TokenType.SUBTRACT)
        {
            switch(CurrentToken().type) {
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

    private Node Relation(){
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
        stack.put(name, value);
        return value;
    }

    public Object getVariable(String name) {
        Object value = stack.get(name);
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
        Node value = null;
        MatchAndEat(TokenType.NEW_VAR);
        if(IsFunctionCall(CurrentToken().type)){
            value = FunctionCall();
        }else{
            value = Expression();
        }

        return new AssignmentNode(name, value, this);
    }

    private Node Assignmet(){
        String name = MatchAndEat(TokenType.WORD).text;
        Node value = null;
        MatchAndEat(TokenType.ASSIGN);
        if(IsFunctionCall(CurrentToken().type)){
            value = FunctionCall();
        }else{
            value = Expression();
        }

        return new AssignmentNode(name, value, this);
    }

    private Node WhileFunc(){
        Node condition, body;
        MatchAndEat(TokenType.WHILE);
        MatchAndEat(TokenType.LEFT_PAREN);
        condition = Expression();
        MatchAndEat(TokenType.RIGHT_PAREN);
        body = Block();
        return new WhileNode(condition, body);
    }

    private Node ForFunc(){
        Node variable, condition, var_increment, body;
        MatchAndEat(TokenType.FOR);
        MatchAndEat(TokenType.LEFT_PAREN);
        variable = NewVar();
        MatchAndEat(TokenType.NEWLINE);
        condition = Expression();
        MatchAndEat(TokenType.NEWLINE);
        var_increment = Assignmet();
        MatchAndEat(TokenType.RIGHT_PAREN);
        body = Block();
        return new ForNode(variable, condition, var_increment, body);

    }

    private boolean IsIfElse()
    {
        TokenType type = CurrentToken().type;
        return type == TokenType.IF || type == TokenType.ELSE;
    }

    private Node IFFunc(){
        Node condition = null, thenPart = null, elsePart = null;

        MatchAndEat(TokenType.IF);
        MatchAndEat(TokenType.LEFT_PAREN);
        condition = Expression();
        MatchAndEat(TokenType.RIGHT_PAREN);
        thenPart = Block();

        if ( CurrentToken().type == TokenType.ELSE ) {

            MatchAndEat(TokenType.ELSE);

            if ( CurrentToken().type == TokenType.IF ) {
                elsePart = IFFunc();
            }
            else elsePart = Block();
        }
        return new IfNode(condition, thenPart, elsePart);
    }

    private Node FunctionDef(){
        MatchAndEat(TokenType.FUNCTION);
        String functionName = MatchAndEat(TokenType.WORD).text;

        MatchAndEat(TokenType.LEFT_PAREN);
        List<Parameter> parameters = DefParameters();
        MatchAndEat(TokenType.RIGHT_PAREN);

        Node functionBody = Block();
        FunctionNode function = new FunctionNode(functionName, functionBody, parameters);
        System.out.println(function.getBody());
        return new AssignmentNode(functionName, function, this);
    }

    private boolean IsFunctionCall(TokenType type){
        return type == TokenType.WORD && NextToken().type == TokenType.LEFT_PAREN;
    }

    private boolean IsFunction(TokenType type){
        return type == TokenType.FUNCTION && NextToken().type == TokenType.WORD;
    }

    private Node FunctionCall(){
        String funcName = MatchAndEat(TokenType.WORD).text;
        Node functionNode = new VariableNode(funcName, this);
        MatchAndEat(TokenType.LEFT_PAREN);
        List<Parameter> parameters = FunctionCallParameters();
        MatchAndEat(TokenType.RIGHT_PAREN);
        return new FunctionCallNode(functionNode, parameters, this);
    }

    private List<Parameter> FunctionCallParameters(){
        List<Parameter> actualParameters = null;
        if (Expression() != null)
        {
            actualParameters = new ArrayList<>();
            actualParameters.add(new Parameter(Expression()));
            while (CurrentToken().type == TokenType.COMMA)
            {
                MatchAndEat(TokenType.COMMA);
                actualParameters.add(new Parameter(Expression()));
            }
        }
        return actualParameters;
    }

    private List<Parameter> DefParameters()
    {
        List<Parameter> parameters = null;
        if (CurrentToken().type == TokenType.WORD){
            parameters = new ArrayList<>();
            parameters.add(new Parameter(MatchAndEat(TokenType.WORD).text));

            while (CurrentToken().type == TokenType.COMMA)
            {
                MatchAndEat(TokenType.COMMA);
                parameters.add(new Parameter(MatchAndEat(TokenType.WORD).text));
            }
        }
        return parameters;
    }

    public Object ExecuteFunction(FunctionNode function, List<BoundParameter> boundParameters)
    {

        Map<String, Object> savedSymbolTable = new HashMap<>(this.stack);

        for (int index = 0; index < boundParameters.size(); index++) {

            BoundParameter param = boundParameters.get(index);
            setVariable(param.getName(), param.getValue());
        }

        Object ret = function.evaluate();

        this.stack = savedSymbolTable;
        return ret;
    }

    private Node PrintFunc(){
        MatchAndEat(TokenType.PRINTLN);
        MatchAndEat(TokenType.LEFT_PAREN);
        Node stuff = Expression();
        MatchAndEat(TokenType.RIGHT_PAREN);
        return new PrintNode(stuff);
    }

    // ------ Statement --------
    private Node Statement(){
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
        }else if(CurrentToken().type == TokenType.FOR){
            nodo = ForFunc();
        }else if(IsIfElse()){
            nodo = IFFunc();
        }else if(IsFunction(type)){
            nodo = FunctionDef();
        }else if(IsFunctionCall(type)){
            nodo = FunctionCall();
        }else if(type == TokenType.PRINTLN){
            nodo = PrintFunc();
        }
        return nodo;
    }


    // ------ Block Func ------
    public BlockNode Block()
    {
        MatchAndEat(TokenType.STARTBLOCK);
        List<Node> statements = new LinkedList<Node>();
        while ( CurrentToken().type != TokenType.ENDBLOCK)
        {
            statements.add(Statement());
        }
        MatchAndEat(TokenType.ENDBLOCK);
        return new BlockNode(statements);
    }

    public void PrintTokens()
    {
        for (Token token: this.tokens)
        {

            if (token.type == TokenType.NUMBER)
            {
                System.out.println("Number....: " + token.text);
            }
            else if (token.type == TokenType.WORD) {
                System.out.println("WORD..: " + token.text);
            }
            else
            {
                System.out.println("Operator..: " + token.type);
            }

        }
    }
}
