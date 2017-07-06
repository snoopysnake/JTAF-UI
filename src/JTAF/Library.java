package JTAF;

import java.util.ArrayList;

/**
 * Created by Michael on 07/04/2017.
 */
public class Library {
    private ArrayList<Command> commands = new ArrayList<>();
    private ArrayList<Function> functions = new ArrayList<>();
    private String libraryName;
    public Library(String libraryName) {
        this.libraryName = libraryName;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void addAllCommands(ArrayList<Command> commands) {
        this.commands.addAll(commands);
    }

    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public void addFunction(Function function) {
        this.functions.add(function);
    }

    public void addAllFunctions(ArrayList<Function> functions) {
        this.functions.addAll(functions);
    }

    public void setFunctions(ArrayList<Function> functions) {
        this.functions = functions;
    }

    public String getLibraryName() {
        return this.libraryName;
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public ArrayList<Function> getFunctions() {
        return this.functions;
    }

    public String toString() {
        return this.libraryName;
    }
}
