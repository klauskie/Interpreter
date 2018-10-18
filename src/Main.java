import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.NullPointerException;

public class Main {

    public static void main(String[] args) {

        initMain(args[0]);
        //init();
    }

    public static void initMain(String path){
        Tokenizer tokenizer = new Tokenizer();

        String source = LeerArchivo(path);
        source += " ";

        Calculator parser = new Calculator(tokenizer.Tokenize(source));
        parser.PrettyPrint(parser.tokens);

        Node script = parser.Block();

        script.evaluate();

        // print map
        System.out.println("Printing variables...");
        parser.stack.forEach((k, v) -> System.out.println((k + ":" + v)));
    }

    public static void init(){
        Tokenizer tokenizer = new Tokenizer();
        String source =
                "{\n" +
                        "rip := 2\n" +
                        "function name (a,b) {\n" +
                        "\treturn := a + b\n" +
                        "}\n" +
                        "e : name(1,2)\n" +
                        "}";


        Calculator calc = new Calculator(tokenizer.Tokenize(source));

        calc.PrettyPrint(calc.tokens);

        Node script = calc.Block();

        // print map
        System.out.println("Printing variables...");
        calc.stack.forEach((k, v) -> System.out.println((k + ":" + v)));

        script.evaluate();

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