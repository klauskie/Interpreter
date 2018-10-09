
public class Main {

    public static void main(String[] args) {
        String expression = "3*5>3+5";
        expression += " ";
        Calculator calc = new Calculator();
        Tokenizer tokenizer = new Tokenizer();
        System.out.println("Expression: " + expression);
        System.out.println("--------------------------");
        calc.tokens = tokenizer.Tokenize(expression);
        calc.PrettyPrint(calc.tokens);
        System.out.println("--------------------------");
        //int result = calc.ArithmeticExpression();
        //System.out.println( result );
        System.out.println(calc.Expresion());
    }
}
