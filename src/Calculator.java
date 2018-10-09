import java.util.List;
import java.util.ArrayList;

public class Calculator {

    public int currentTokenPosition = 0;
    public List<Token> tokens;

    //~~~~Token Manipulation Methods Start~~~~
    public Token GetToken(int offset)
    {
        if (currentTokenPosition + offset >= tokens.size())
        {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(currentTokenPosition + offset);
    }

    public Token NextToken()
    {
        return GetToken(1);
    }

    public Token CurrentToken()
    {
        return GetToken(0);
    }

    // Just eats the token(s) given in the offset
    public void EatToken(int offset)
    {
        currentTokenPosition = currentTokenPosition + offset;
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

    private int Add() {
        MatchAndEat(TokenType.ADD);
        return Term();
    }

    private int Subtract() {
        MatchAndEat(TokenType.SUBTRACT);
        return Term();
    }

    private int Multiply() {
        MatchAndEat(TokenType.MULTIPLY);
        return Factor();
    }

    private int Divide() {
        MatchAndEat(TokenType.DIVIDE);
        return Factor();
    }

    public int Factor()
    {
        int result = 0;
        if (CurrentToken().type == TokenType.LEFT_PAREN)
        {
            MatchAndEat(TokenType.LEFT_PAREN);
            result = ArithmeticExpression();
            MatchAndEat(TokenType.RIGHT_PAREN);
        }
        else if (CurrentToken().type == TokenType.NUMBER)
        {
            result = new Integer(CurrentToken().text).intValue();
            MatchAndEat(TokenType.NUMBER);
        }
        return result;
    }
    public int Term()
    {
        int result = Factor();
        while ( CurrentToken().type == TokenType.MULTIPLY ||
                CurrentToken().type == TokenType.DIVIDE )
        {
            switch(CurrentToken().type)
            {
                case MULTIPLY:
                    result = result * Multiply();
                    break;
                case DIVIDE:
                    result = result / Divide();
                    break;
            }
        }
        return result;
    }

    public int ArithmeticExpression()
    {
        int result = Term();
        while (CurrentToken().type == TokenType.ADD ||
                CurrentToken().type == TokenType.SUBTRACT)
        {
            switch(CurrentToken().type)
            {
                case ADD:
                    result = result + Add();
                    break;
                case SUBTRACT:
                    result = result - Subtract();
                    break;
            }
        }
        return result;
    }

    public void PrettyPrint(List<Token> tokens)
    {
        //int numberCount = 0;
        //int opCount = 0;
        for (Token token: tokens)
        {
            System.out.println("TOKEN....: " + token.text);
            /*
            if (token.type == TokenType.NUMBER)
            {
                System.out.println("Number....: " + token.text);
                numberCount++;
            }
            else if(token.type == TokenType.LESS_THAN)
            {
                System.out.println("Operator..: " + token.type);
                opCount++;
            }else{
                System.out.println("Operator..: " + token.type);
                opCount++;
            }
            */
        }
        //System.out.println("You have got "+ numberCount + " different number and " + opCount + " operators.");
    }
}
