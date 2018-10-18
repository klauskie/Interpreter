import java.util.List;
import java.util.ArrayList;

public class Tokenizer {

    private boolean IsOperator(char chr)
    {
        return chr == '+' || chr == '-' ||
                chr == '*' || chr == '/' ||
                chr == '^' || chr == '%';
    }

    private TokenType FindOperatorType(char firstOperator)
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
            case '^':
                type = TokenType.EXPONENT;
                break;
            case '%':
                type = TokenType.MODULUS;
                break;
        }
        return type;
    }

    private boolean IsParen(char chr) {
        return chr == '(' || chr == ')';
    }

    private TokenType FindParenType(char chr)
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

    private boolean IsLogicOperator(char chr) {
        return chr == '<' || chr == '>' || chr == '=' || chr == '!';
    }

    private TokenType FindLogicType(String op) {
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

    private boolean IsBlock(char chr){
        return chr == '{' || chr == '}';
    }

    private TokenType FindBlockType(char chr){
        TokenType type = TokenType.UNKNOWN;
        switch (chr){
            case '{':
                type = TokenType.STARTBLOCK;
                break;
            case '}':
                type = TokenType.ENDBLOCK;
                break;
        }
        return type;
    }

    private boolean IsABC(char chr){
        return Character.isLetter(chr);
    }

    private TokenType KeywordType(String word){
        TokenType result = TokenType.WORD;
        if(word.equals("while")){
            result = TokenType.WHILE;
        }else if(word.equals("for")){
            result = TokenType.FOR;
        }else if(word.equals("PI")){
            result = TokenType.WORD;
        }else if(word.equals("E")){
            result = TokenType.WORD;
        }else if(word.equals("END")){
            result = TokenType.ENDBLOCK;
        }else if(word.equals("if")){
            result = TokenType.IF;
        }else if(word.equals("else")){
            result = TokenType.ELSE;
        }else if(word.equals("function")){
            result = TokenType.FUNCTION;
        }else if(word.equals("println")){
            result = TokenType.PRINTLN;
        }
        return result;
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
                    if (IsOperator(chr)) { // if char is an arithmetic operator
                        TokenType opType = FindOperatorType(chr);
                        tokens.add( new Token(Character.toString(chr), opType) );
                    }
                    else if (IsLogicOperator(chr)) { // if char is an arithmetic operator
                        state = TokenizeState.OPERATOR;
                        index--;
                    }
                    else if (IsParen(chr)) { // if chr is a parentesis
                        TokenType parenType = FindParenType(chr);
                        tokens.add(new Token(Character.toString(chr), parenType));
                    }
                    else if (Character.isDigit(chr)) { // if chr is a number
                        token += chr;
                        state = TokenizeState.NUMBER;
                    }
                    else if(IsABC(chr)) { // if chr is a letter
                        token += chr;
                        state = TokenizeState.WORD;
                    }
                    else if (chr == ':') { // if there is an ':='
                        token += chr;
                        state = TokenizeState.ASSIGN;
                    }
                    else if (chr == ';'){ // if chr is ';'
                        token = ";";
                        TokenType new_line = TokenType.NEWLINE;
                        tokens.add(new Token(token, new_line));
                        token = "";
                    }else if(chr == ','){ // if chr is a comma
                        token += chr;
                        TokenType comma = TokenType.COMMA;
                        tokens.add(new Token(token, comma));
                        token = "";
                    }else if(IsBlock(chr)){
                        token += chr;
                        tokens.add(new Token(token, FindBlockType(chr)));
                        token = "";

                    }
                    break;
                case NUMBER:
                    if (Character.isDigit(chr)) {
                        token += chr;
                    } else if(!token.equals("")){
                        tokens.add(new Token(token, TokenType.NUMBER));
                        token = "";
                        if(IsLogicOperator(chr)){
                            state = TokenizeState.OPERATOR;
                        }else{
                            state = TokenizeState.DEFAULT;
                        }
                        index--;
                    }
                    break;
                case OPERATOR:
                    if(IsLogicOperator(chr)){
                        token += chr;
                    }else {
                        TokenType operator = FindLogicType(token);
                        tokens.add(new Token(token, operator));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        index--;
                    }
                    break;
                case WORD:
                    if(IsABC(chr)){
                        token += chr;
                    }else{
                        TokenType word = KeywordType(token);
                        tokens.add(new Token(token, word));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        index--;
                    }
                    break;
                case ASSIGN:
                    if(chr == '='){
                        token += chr;
                    }if(token.equals(":=")){
                        TokenType new_var = TokenType.NEW_VAR;
                        tokens.add(new Token(token, new_var));
                        token = "";
                        state = TokenizeState.DEFAULT;
                    } if(token.equals(":")){
                        TokenType type = TokenType.ASSIGN;
                        tokens.add(new Token(token, type));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        index--;
                    }
                    break;
            }
        } // r := 4;
        return tokens;
    }

}
