import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        String sourceCode = ReadFile(args[0]);
        Interpret(sourceCode, true);
    }


    public static void Interpret(String source, boolean debug)
    {
        Tokenizer tokenizer = new Tokenizer();
        Calculator parser = new Calculator(tokenizer.Tokenize(source));
        if (debug) DumpTokens(parser);

        System.out.println(parser.Expresion().evaluate());
    }

    public static void DumpTokens(Parser parser)
    {
        for (Token token: parser.getTokens())
            System.out.println("Type: " + token.type + " Text: " + token.text+" ");
        System.out.println();
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
}


// variables and while loops : pg. 130