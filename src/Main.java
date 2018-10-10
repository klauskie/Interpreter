
public class Main {

    public static void main(String[] args) {
        String expression = "hola := 3;";
        expression += " ";
        Calculator calc = new Calculator();
        Tokenizer tokenizer = new Tokenizer();
        System.out.println("Expression: " + expression);
        System.out.println("--------------------------");
        calc.tokens = tokenizer.Tokenize(expression);
        calc.PrettyPrint(calc.tokens);
        System.out.println("--------------------------");
        //System.out.println(calc.Expresion().evaluate());

    }
}
