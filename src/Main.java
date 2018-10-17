import java.util.List;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        /*
        String expression = "0*6";
        expression += " ";
        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator(tokenizer.Tokenize(expression));

        System.out.println("Expression: " + expression);
        System.out.println("--------------------------");
        calc.PrettyPrint(calc.tokens);
        System.out.println("--------------------------");
        System.out.println(calc.Expression().evaluate());
        */
        //init();
        init2();
    }

    public static void init(){
        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator();

        List<String> expressions = new LinkedList<String>();
        //expressions.add("(3+4) ");
        //expressions.add("(4 * 6) ");
        //expressions.add("(3 != 3)  ");
        expressions.add("var := 3  ");

        List<Node> commandList = new LinkedList<Node>();
        // Interpretar
        for (String expression: expressions) {
            System.out.println("Expression: " + expression);
            calc.currentTokenPosition = 0;
            calc.tokens = tokenizer.Tokenize((expression));
            commandList.add(calc.Expression());
        }

        for (Node nodo: commandList) {
            System.out.println("Expression Result: " + nodo.evaluate());
        }
    }

    public static void init2(){
        Tokenizer tokenizer = new Tokenizer();
        Calculator calc = new Calculator();

        calc.tokens = tokenizer.Tokenize(" ver := 4 ver : ver + 5 END ");
        //calc.PrettyPrint(calc.tokens);

        List<Node> script = calc.Block();
        for (Node statement:script)
            System.out.println(statement.evaluate());



        // print map
        calc.symbolTable.forEach((k, v) -> System.out.println((k + ":" + v)));
    }

/*
    public static void Interpret(String source, boolean debug)
    {
        Tokenizer tokenizer = new Tokenizer();
        Calculator parser = new Calculator(tokenizer.Tokenize(source));
        if (debug) DumpTokens(parser);

        System.out.println(parser.Expression().evaluate());
    }

    public static void DumpTokens(Parser parser)
    {
        for (Token token: parser.getTokens())
            System.out.println("Type: " + token.type + " Text: " + token.text+" ");
        System.out.println();
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
            }

        }
        return null;
    }


    private static String ReadFile(String path) {
        FileInputStream stream = null;
        InputStreamReader input = null;
        try {
            stream = new FileInputStream(path);
            input = new InputStreamReader(stream, Charset.defaultCharset());
            Reader reader = new BufferedReader(input);
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            //Keep the space in the end
            builder.append(" ");
            return builder.toString();
        } catch (FileNotFoundException e) {
            String errMsg = "FILE NOT FOUND. ";
            String sourceInfo = "Error in Interpreter.java->"
                    + "ReadFile(String path) method. ";
            System.out.println(sourceInfo + errMsg);
            System.exit(0);
        } catch (IOException e) {
            String errMsg = "Error while reading the script. ";
            String sourceInfo = "Error in Interpreter.java->"
                    + "ReadFile(String path) method. ";
            System.out.println(sourceInfo + errMsg);
            System.exit(0);
        } catch (Exception e) {
            String errMsg = "Error while reading the script. ";
            String sourceInfo = "Error in Interpreter.java->"
                    + "ReadFile(String path) method. ";
            System.out.println(sourceInfo + errMsg + e);
            System.exit(0);
        } finally {
            try {
                input.close();
                stream.close();
            } catch (Exception e) {
                String errMsg = "Error while closing a stream or a stream reader. ";
                String sourceInfo = "Error in Interpreter.java->"
                        + "ReadFile(String path) method. ";
                System.out.println(sourceInfo + errMsg + e);
                System.exit(0);
            }
        }
        return null;
    }
    */
}


// variables and while loops : pg. 130