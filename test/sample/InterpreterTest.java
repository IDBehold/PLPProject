package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Util.Command;

import java.util.List;

import static org.mockito.Mockito.mock;

public class InterpreterTest {
    private Painter painter;
    private Interpreter interpreter;

    @Before
    public void setup() {
        painter = mock(Painter.class);
        interpreter = new Interpreter(painter);
    }

    @Test
    public void splitIntoCommands_correctAmountOfCommands() {
        Assert.assertEquals(1, interpreter.splitIntoCommands("(s)").size());
        Assert.assertEquals(2, interpreter.splitIntoCommands("(s)\n(test (1 2) 3)").size());
        Assert.assertEquals(3, interpreter.splitIntoCommands("    (s)\n(te)(s)").size());
        Assert.assertEquals(4, interpreter.splitIntoCommands("(s)\n(s)\n(s)\n(s)").size());
    }

    @Test
    public void splitIntoCommands_correctContent_outerParenthesesRemoved() {
        Assert.assertEquals("s", interpreter.splitIntoCommands("(s)").get(0));
        Assert.assertEquals("test (1 2) 3", interpreter.splitIntoCommands("(s)\n(test (1 2) 3)").get(1));
        Assert.assertEquals("s", interpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(0));
        Assert.assertEquals("test 1 2 3", interpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(1));
        Assert.assertEquals("Z", interpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(2));
        Assert.assertEquals("22", interpreter.splitIntoCommands("(s)\n(s)\n(s)\n(22)").get(3));
    }

    @Test
    public void createCommands_correctCommandNames(){
        List<String> commandStrings = interpreter.splitIntoCommands("(function (12 12) 35)\n(test (1 2) (2 99) text)(foo 1 2 3)(foo 1 2 3)");
        List<Command> commandList = interpreter.createCommands(commandStrings);
        Assert.assertEquals("function", commandList.get(0).getName());
        Assert.assertEquals("test", commandList.get(1).getName());
        Assert.assertEquals("foo", commandList.get(2).getName());
        Assert.assertEquals("foo", commandList.get(3).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void createCommands_commandNameWithNumbers_invalidCommandName() {
        List<String> commands = interpreter.splitIntoCommands("(function(12 12)35)");
        interpreter.createCommands(commands);
    }

    @Test(expected = IllegalStateException.class)
    public void createCommands_commandNameWithSpecialCharacters_invalidCommandName() {
        List<String> commands = interpreter.splitIntoCommands("(function!.( 1212)35)");
        interpreter.createCommands(commands);
    }

    @Test(expected = IllegalStateException.class)
    public void createCommands_noSpaces_invalidCommandName() {
        List<String> commands = interpreter.splitIntoCommands("(function(1212)35)");
        interpreter.createCommands(commands);
    }

    @Test
    public void createCommands_correctCommandParameterCount_IntegerParameters() {
        List<String> commands = interpreter.splitIntoCommands("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals(3, commandList.get(0).getParameters().size());
        Assert.assertEquals(2, commandList.get(1).getParameters().size());
        Assert.assertEquals(4, commandList.get(2).getParameters().size());
    }
    @Test
    public void createCommands_correctCommandParameter_IntegerParameters() {
        List<String> commands = interpreter.splitIntoCommands("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals("12", commandList.get(0).getParameters().get(0));
        Assert.assertEquals("13", commandList.get(0).getParameters().get(1));
        Assert.assertEquals("35", commandList.get(0).getParameters().get(2));
        Assert.assertEquals("35", commandList.get(1).getParameters().get(0));
        Assert.assertEquals("66", commandList.get(1).getParameters().get(1));
        Assert.assertEquals("1", commandList.get(2).getParameters().get(0));
        Assert.assertEquals("2", commandList.get(2).getParameters().get(1));
        Assert.assertEquals("2", commandList.get(2).getParameters().get(2));
        Assert.assertEquals("99", commandList.get(2).getParameters().get(3));
    }

    @Test
    public void createCommands_correctCommandParameterCount_Parentheses() {
        List<String> commands = interpreter.splitIntoCommands("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) test 10)");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals(2, commandList.get(0).getParameters().size());
        Assert.assertEquals(2, commandList.get(1).getParameters().size());
        Assert.assertEquals(3, commandList.get(2).getParameters().size());
    }

    @Test
    public void createCommands_correctCommandParameter() {
        List<String> commands = interpreter.splitIntoCommands("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) test 10)");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals("12 12", commandList.get(0).getParameters().get(0));
        Assert.assertEquals("35", commandList.get(0).getParameters().get(1));
        Assert.assertEquals("35 66", commandList.get(1).getParameters().get(0));
        Assert.assertEquals("2 2", commandList.get(1).getParameters().get(1));
        Assert.assertEquals("2 100", commandList.get(2).getParameters().get(0));
        Assert.assertEquals("test", commandList.get(2).getParameters().get(1));
        Assert.assertEquals("10", commandList.get(2).getParameters().get(2));
    }

    @Test
    public void createCommands_higherOrderFunctions_parameterCount(){
        List<String> commands = interpreter.splitIntoCommands("(function (foo 12 12) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals(2, commandList.get(0).getParameters().size());
        Assert.assertEquals(3, commandList.get(1).getParameters().size());
    }

    @Test
    public void createCommands_higherOrderFunctions_parameterContent(){
        List<String> commands = interpreter.splitIntoCommands("(function (foo 12 12) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals("foo 12 12", commandList.get(0).getParameters().get(0));
        Assert.assertEquals("LINE (2 2) (22 2)", commandList.get(0).getParameters().get(1));
        Assert.assertEquals("RED", commandList.get(1).getParameters().get(0));
        Assert.assertEquals("CIRCLE (35 66) 5", commandList.get(1).getParameters().get(1));
        Assert.assertEquals("LINE (2 2) (5 5)", commandList.get(1).getParameters().get(2));
    }


}