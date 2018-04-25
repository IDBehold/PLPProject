package sample;

import sample.Util.Command;
import scala.Char;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private Painter painter;

    public Interpreter(Painter painter) {
        //Default
        this.painter = painter;
    }

    public void interpret(String s) {


    }

    public List<String> splitIntoCommands(String rawInput) {
        List<String> strings = new ArrayList<>();
        int openBracketsCounter = 0;
        int pos = 0;
        int startIndex = 0;
        int endIndex = 0;

        while (pos < rawInput.length()) {
            if (rawInput.charAt(pos) == '(' && openBracketsCounter == 0) {
                startIndex = pos + 1;
                openBracketsCounter++;
            } else if (rawInput.charAt(pos) == '(') {
                openBracketsCounter++;
            } else if (rawInput.charAt(pos) == ')') {
                openBracketsCounter--;
            }

            if (openBracketsCounter == 0 && rawInput.charAt(pos) == ')') {
                endIndex = pos;
                strings.add(rawInput.substring(startIndex, endIndex));
            }
            pos++;
        }

        return strings;
    }

    public List<Command> createCommands(List<String> commands) {
        List<Command> result = new ArrayList<>();
        int counter = 0;
        while(counter < commands.size()) {
            String command = commands.get(counter);
            String commandName = getCommandName(command);
            if(commandName.toLowerCase().equals("draw") || commandName.toLowerCase().equals("fill")){
                List<String> nestedCommands = splitIntoCommands(command);
                Command e = new Command(getCommandName(command));
                e.setHigherOrderFunctions(createCommands(nestedCommands));
                result.add(e);
            } else {
                Command e = new Command(getCommandName(command));
                e.setParameters(getCommandParameters(command));
                result.add(e);
            }
            counter++;
        }
        return result;
    }

    private String getCommandName(String command) {
        String[] split = command.split(" ");

        if (split.length > 0 && split[0].matches("[a-zA-Z]+")) {
            return split[0];
        }
        throw new IllegalStateException("No valid command name was found");
    }

    public List<String> getCommandParameters(String command) {
        List<String> result = new ArrayList<>();
        int pos = 0;
        int startIndex = 0;
        int endIndex = 0;
        boolean findEnd = false;
        boolean lookForStartingSpace = true;
        int openBracketsCounter = 0;

        while (pos < command.length()) {
            if (!Character.toString(command.charAt(pos)).matches("[a-zA-Z0-9\\s]|[(]|[)]")) {
                throw new IllegalStateException("Invalid command");
            }

            if (openBracketsCounter == 0) {
                if (command.charAt(pos) == ' ' && lookForStartingSpace) {
                    lookForStartingSpace = false;
                    startIndex = pos + 1;
                } else if (command.charAt(pos) == ' ' && !lookForStartingSpace) {
                    lookForStartingSpace = true;
                    endIndex = pos;
                    result.add(command.substring(startIndex, endIndex));
                    if (Character.toString(command.charAt(pos + 1)).matches("[0-9]")) {
                        startIndex = pos + 1;
                        lookForStartingSpace = false;
                    }
                } else if (pos == command.length() - 1 && Character.toString(command.charAt(pos)).matches("[0-9]") && !lookForStartingSpace) {
                    endIndex = pos + 1;
                    result.add(command.substring(startIndex, endIndex));
                }
            }

            if (command.charAt(pos) == '(') {
                lookForStartingSpace = true;
                openBracketsCounter++;
                startIndex = pos + 1;
            } else if (command.charAt(pos) == ')') {
                openBracketsCounter--;
            }

            if (openBracketsCounter == 0 && command.charAt(pos) == ')') {
                endIndex = pos;
                result.add(command.substring(startIndex, endIndex));
            }
            pos++;
        }

        return result;
    }
}
