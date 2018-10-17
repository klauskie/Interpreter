import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.NullPointerException;

public class Main {

    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator();

        String path = args[0];
        String source = LeerArchivo(path);
        source += " ";

        System.out.println(source);

        calc.tokens = tokenizer.Tokenize(source);
        //calc.PrettyPrint(calc.tokens);

        Node script = calc.Block();

        script.evaluate();
    }

    public static void init3(){
        String expression = "2*2";
        expression += " ";
        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator();

        calc.tokens = tokenizer.Tokenize(expression);

        System.out.println("Expression: " + expression);
        System.out.println("--------------------------");
        calc.PrettyPrint(calc.tokens);
        System.out.println("--------------------------");
        System.out.println(calc.Expression().evaluate());
    }


    public static void init2(){
        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator();

        String source =
                "var := 4 " +
                "while var != 0 " +
                "var : var - 1 END " +
                "END ";

        calc.tokens = tokenizer.Tokenize(source);
        calc.PrettyPrint(calc.tokens);

        Node script = calc.Block();

        script.evaluate();


        // print map
        calc.symbolTable.forEach((k, v) -> System.out.println((k + ":" + v)));
    }


    private static String LeerArchivo(String path){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(" ");
                line = br.readLine();
            }
            return sb.toString();
        } catch(FileNotFoundException e){
            System.out.println("File " + path + " not found...");
        } catch(IOException e) {
            System.out.println("IOException..." + e.getMessage());
        } finally {
            try{
                br.close();
            }catch(IOException e){
                System.out.println("IOException... Dentro de Finally... " + e.getMessage());
            }catch(NullPointerException e){
                System.out.println("NullPointerException... Dentro de Finally..." + e.getMessage());
            }

        }
        return null;
    }

}


// variables and while loops : pg. 130