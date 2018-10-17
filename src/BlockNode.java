import java.util.List;

public class BlockNode extends Node{
    private List<Node> statements;

    public BlockNode(List<Node> statements){
        this.statements = statements;
    }

    public Object evaluate() {
        Object temp = null;
        for (Node statement : statements)
        {
            temp = statement.evaluate();
            System.out.println(temp);
        }
        return temp;
    }

    protected List<Node> getStatements() {
        return this.statements;
    }

    public String toString() {
        String str = "";
        for (Node statement: statements)
            str = str + statement + "\n";
        return str;
    }
}
