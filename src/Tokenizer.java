import java.util.List;
import java.util.ArrayList;

public class Tokenizer {

    public boolean IsOp(char chr)
    {
        return chr == '+' || chr == '-' ||
                chr == '*' || chr == '/';
    }

    public TokenType FindOpType(char firstOperator)
    {
        TokenType type = TokenType.UNKNOWN;
        switch(firstOperator)
        {
            case '+':
                type = TokenType.ADD;
                break;
            case '-':
                type = TokenType.SUBTRACT;
                break;
            case '*':
                type = TokenType.MULTIPLY;
                break;
            case '/':
                type = TokenType.DIVIDE;
                break;
        }
        return type;
    }

    public boolean IsParen(char chr) {
        return chr == '(' || chr == ')';
    }

    public TokenType FindParenType(char chr)
    {
        TokenType type = TokenType.UNKNOWN;
        switch(chr)
        {
            case '(':
                type = TokenType.LEFT_PAREN;
                break;
            case ')':
                type = TokenType.RIGHT_PAREN;
                break;
        }
        return type;
    }

    public boolean IsLogicOp(char chr) {
        return chr == '<' || chr == '>' || chr == '=' || chr == '!';
    }

    public TokenType FindLogicType(String op) {
        TokenType type = TokenType.UNKNOWN;
        if(op.equals("<")){
            type = TokenType.LESS_THAN;
        }else if(op.equals(">")){
            type = TokenType.MORE_THAN;
        }else if(op.equals("==")){
            type = TokenType.EQUALS;
        }else if(op.equals("!=")){
            type = TokenType.NOT_EQUALS;
        }else if(op.equals("<=")){
            type = TokenType.LESS_EQUALS;
        }else if(op.equals(">=")){
            type = TokenType.MORE_EQUALS;
        }

        return type;
    }

    public List<Token> Tokenize(String source)
    {
        List<Token> tokens = new ArrayList<Token>();
        String token = "";
        TokenizeState state = TokenizeState.DEFAULT;
        for (int index = 0; index < source.length(); index++)
        {
            char chr = source.charAt(index);
            switch(state)
            {
                case DEFAULT:
                    TokenType opType = FindOpType(chr);
                    if (IsOp(chr))
                    {
                        tokens.add( new Token(Character.toString(chr), opType) );
                    }
                    else if (IsParen(chr))
                    {
                        TokenType parenType = FindParenType(chr);
                        tokens.add(new Token(Character.toString(chr), parenType));
                    }
                    else if (Character.isDigit(chr))
                    {
                        token += chr;
                        state = TokenizeState.NUMBER;
                    }
                    break;
                case NUMBER:
                    if (Character.isDigit(chr))
                    {
                        token += chr;
                    }
                    else
                    {
                        tokens.add(new Token(token, TokenType.NUMBER));
                        token = "";
                        if(IsLogicOp(chr)){
                            state = TokenizeState.OPERATOR;
                        }else{
                            state = TokenizeState.DEFAULT;
                        }
                        index--;
                    }
                    break;
                case OPERATOR:
                    if(IsLogicOp(chr)){
                        token += chr;
                    }else{
                        // saber que pedo con token
                        TokenType operator = FindLogicType(token);
                        tokens.add(new Token(token, operator));
                        token = "";
                        state = TokenizeState.NUMBER;
                        index--;
                    }
                    break;
            }
        }
        return tokens;
    }

}
