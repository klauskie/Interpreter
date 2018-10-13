import java.util.List;

public class Parser {
    public List<Token> tokens;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Token> getTokens(){
        return tokens;
    }

    public Node Expresion(){ return null;}
}
