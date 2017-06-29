package JTAF;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michael on 06/28/2017.
 */
public class Command {
    private String commandName;
    private String commandClass;
    private String commandUsage;
    private ArrayList<Parameter> requiredParameters = new ArrayList<>();
    private ArrayList<Parameter> optionalParameters = new ArrayList<>();
    private ArrayList<Parameter> commandResults = new ArrayList<>();

    public Command(String commandName, String commandClass) {
        setCommandName(commandName);
        setCommandClass(commandClass);
        setCommandUsage("");
    }

    public Command(String commandName, String commandClass, String commandUsage) {
        setCommandName(commandName);
        setCommandClass(commandClass);
        setCommandUsage(commandUsage);
    }

    public String getCommandName() {
        return commandName;
    }

    private void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandClass() {
        return commandClass;
    }

    private void setCommandClass(String commandClass) {
        this.commandClass = commandClass;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public void setCommandUsage(String commandUsage) {
        this.commandUsage = commandUsage;
    }

    public boolean hasCommandUsage() {
        return (commandUsage != null);
    }

    public ArrayList<Parameter> getCommandResults() {
        return commandResults;
    }

    public void addCommandResult(Parameter parameter) {
        commandResults.add(parameter);
    }

    public boolean hasCommandResults() {
        return !commandResults.isEmpty();
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

}
