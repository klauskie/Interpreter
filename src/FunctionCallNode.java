import java.util.List;
import java.util.ArrayList;

public class FunctionCallNode extends Node {

    public Node name;
    public List<Parameter> actualParameters;
    public Calculator parser;

    public FunctionCallNode(Node name, List<Parameter> actualParameters, Calculator parser) {
        this.name = name;
        this.actualParameters = actualParameters;
        this.parser = parser;
    }

    @Override
    public Object evaluate(){
        FunctionNode function = (FunctionNode) this.name.evaluate();
        List<BoundParameter> boundParameters = new ArrayList<>();

        if(function.getParameters() != null){
            if(this.actualParameters.size() != function.getParameters().size()){
                System.out.println("Error: number of parameters mismatch.");
            }else{
                for(int i = 0; i < this.actualParameters.size(); i++){
                    String par_name = function.getParameters().get(i).getName();
                    Object par_value = actualParameters.get(i).getValue();
                    if (par_value instanceof FunctionNode) {
                        par_value = ((FunctionNode) par_value).evaluate();
                    }
                    boundParameters.add(new BoundParameter(par_name, par_value));
                }
            }
        }

        return this.parser.ExecuteFunction(function, boundParameters);
    }
}