package JTAF;

import java.util.ArrayList;

/**
 * Created by Michael on 06/28/2017.
 */
public class Function {
    private String functionName;
    private String functionUsage;
    private ArrayList<Parameter> requiredParameters = new ArrayList<>();
    private ArrayList<Parameter> optionalParameters = new ArrayList<>();
    private ArrayList<String> functionBody = new ArrayList<>();
    private ArrayList<Parameter> functionResults = new ArrayList<>();

    public Function(String functionName) {
        setFunctionName(functionName);
        setFunctionUsage(null);
    }

    public Function(String functionName, String functionUsage) {
        setFunctionName(functionName);
        setFunctionUsage(functionUsage);
    }

    public String getFunctionName() {
        return functionName;
    }

    private void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionUsage() {
        return functionUsage;
    }

    public void setFunctionUsage(String functionUsage) {
        this.functionUsage = functionUsage;
    }

    public boolean hasFunctionUsage() {
        return (functionUsage != null);
    }

    public ArrayList<Parameter> getRequiredParameters() {
        return requiredParameters;
    }

    public void addRequiredParameters(Parameter parameter) {
        requiredParameters.add(parameter);
    }

    public boolean hasRequiredParameters() {
        return !requiredParameters.isEmpty();
    }

    public ArrayList<Parameter> getOptionalParameters() {
        return optionalParameters;
    }

    public void addOptionalParameters(Parameter parameter) {
        optionalParameters.add(parameter);
    }

    public boolean hasOptionalParameters() {
        return !optionalParameters.isEmpty();
    }

    public ArrayList<Parameter> getFunctionResults() {
        return functionResults;
    }

    public void addFunctionResult(Parameter parameter) {
        functionResults.add(parameter);
    }

    public boolean hasFunctionResults() {
        return !functionResults.isEmpty();
    }

    public ArrayList<String> getFunctionBody() {
        return functionBody;
    }

    public void addToFunctionBody(String commandName) {
        this.functionBody.add(commandName);
    }

    public boolean hasFunctionBody() {
        return !functionBody.isEmpty();
    }
}
