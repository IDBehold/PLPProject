package sample.Util;

import java.util.List;

public class Command {
    private String name;
    private List<String> parameters;

    public Command(String name, List<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }
}