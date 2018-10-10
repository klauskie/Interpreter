
public class Main {

    public static void main(String[] args) {
        String expression = "((5+1)*100-2+3)>501";
        expression += " ";
        Calculator calc = new Calculator();
        Tokenizer tokenizer = new Tokenizer();
        System.out.println("Expression: " + expression);
        System.out.println("--------------------------");
        calc.tokens = tokenizer.Tokenize(expression);
        calc.PrettyPrint(calc.tokens);
        System.out.println("--------------------------");
        System.out.println(calc.Expresion().evaluate());

    }
}
