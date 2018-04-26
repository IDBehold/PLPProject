package sample;

import sample.Util.Command;
import scala.Char;
import scala.Draw;

import java.awt.*;
import java.lang.reflect.Field;
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
        List<String> commandstrings = splitIntoCommands(s);
        List<Command> commands = createCommands(commandstrings);

        for (Command command : commands) {
            switch (command.getName().toLowerCase()) {
                case "bounding-box": {
                    String parameter1 = command.getParameter(0);
                    String[] split1 = parameter1.split(" ");
                    String parameter2 = command.getParameter(1);
                    String[] split2 = parameter2.split(" ");

                    painter.setBoundingBox(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                    break;
                }
                case "line": {
                    String parameter1 = command.getParameter(0);
                    String[] split1 = parameter1.split(" ");
                    String parameter2 = command.getParameter(1);
                    String[] split2 = parameter2.split(" ");

                    painter.drawLine(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                    break;
                }
                case "circle": {
                    String parameter1 = command.getParameter(0);
                    String[] split1 = parameter1.split(" ");
                    String parameter2 = command.getParameter(1);
                    painter.drawCircle(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(parameter2));
                    break;
                }
                case "rectangle": {
                    String parameter1 = command.getParameter(0);
                    String[] split1 = parameter1.split(" ");
                    String parameter2 = command.getParameter(1);
                    String[] split2 = parameter2.split(" ");

                    painter.drawRectangle(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                    break;
                }
                case "text-at": {
                    String parameter1 = command.getParameter(0);
                    String[] split1 = parameter1.split(" ");
                    String parameter2 = command.getParameter(1);
                    painter.drawTextAt(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), parameter2);
                    break;
                }
                case "draw": {
                    String color = command.getParameter(0);
                    Draw.ShapeList list = new Draw.Nil();
                    for (Command c : command.getHigherOrderFunctions()) {
                        list = appendShape(c, list);
                    }
                    painter.drawShapes(parseColor(color), list);
                    break;
                }
//                case "fill":
//                    painter.fillShape();
//                    break;
                default:
                    throw new IllegalArgumentException(command.getName() + " is not a valid command");
            }
        }

    }

    private Draw.ShapeList appendShape(Command command, Draw.ShapeList tail) {
        switch (command.getName().toLowerCase()) {
            case "line": {
                String parameter1 = command.getParameter(0);
                String[] split1 = parameter1.split(" ");
                String parameter2 = command.getParameter(1);
                String[] split2 = parameter2.split(" ");
                return new Draw.Cons(new Draw.Line(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1])), tail);
            }
            case "rectangle": {
                String parameter1 = command.getParameter(0);
                String[] split1 = parameter1.split(" ");
                String parameter2 = command.getParameter(1);
                String[] split2 = parameter2.split(" ");
                return new Draw.Cons(new Draw.Rectangle(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1])), tail);
            }
            case "text-at": {
                String parameter1 = command.getParameter(0);
                String[] split1 = parameter1.split(" ");
                String parameter2 = command.getParameter(1);
                return new Draw.Cons(new Draw.TextAt(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), parameter2), tail);
            }
            case "circle": {
                String parameter1 = command.getParameter(0);
                String[] split1 = parameter1.split(" ");
                String parameter2 = command.getParameter(1);
                return new Draw.Cons(new Draw.Circle(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(parameter2)), tail);
            }
            default: {
                throw new IllegalArgumentException(command.getName() + " is not a valid higher order command");
            }
        }
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
        while (counter < commands.size()) {
            String command = commands.get(counter);
            String commandName = getCommandName(command);
            if (commandName.toLowerCase().equals("draw") || commandName.toLowerCase().equals("fill")) {
                List<String> nestedCommands = splitIntoCommands(command);
                List<String> parameters = getCommandParametersInHigherOrderFunctions(command);
                Command e = new Command(getCommandName(command));
                e.setHigherOrderFunctions(createCommands(nestedCommands));
                e.setParameters(parameters);
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

    private List<String> getCommandParametersInHigherOrderFunctions(String command) {
        List<String> params = getCommandParameters(command);
        List<String> result = new ArrayList<>();
        for (String s : params) {
            if (s.matches("[a-zA-Z]+")) {
                result.add(s);
            }
        }
        return result;
    }

    private String getCommandName(String command) {
        String[] split = command.split("[\\s\\(]");

        if (split.length > 0 && split[0].matches("[a-zA-Z\\-]+")) {
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
            if (!Character.toString(command.charAt(pos)).matches("[a-zA-Z0-9\\s\\-()]")) {
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
                } else if (pos == command.length() - 1 && Character.toString(command.charAt(pos)).matches("[a-zA-Z0-9]") && !lookForStartingSpace) {
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

    public Color parseColor(String colorName) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(colorName.toUpperCase());
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = Color.BLACK;
        }
        return color;
    }
}

