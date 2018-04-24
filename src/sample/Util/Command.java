package sample.Util;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String name;
    private List<String> parameters;
    private List<Command> higherOrderFunctions;

    public Command(String name) {
        this.name = name;
        parameters = new ArrayList<String>();
        higherOrderFunctions = new ArrayList<Command>();
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<Command> getHigherOrderFunctions() {
        return higherOrderFunctions;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void setHigherOrderFunctions(List<Command> higherOrderFunctions) {
        this.higherOrderFunctions = higherOrderFunctions;
    }
}