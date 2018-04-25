package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Util.Command;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        Assert.assertEquals("12", commandList.get(0).getParameter(0));
        Assert.assertEquals("13", commandList.get(0).getParameter(1));
        Assert.assertEquals("35", commandList.get(0).getParameter(2));
        Assert.assertEquals("35", commandList.get(1).getParameter(0));
        Assert.assertEquals("66", commandList.get(1).getParameter(1));
        Assert.assertEquals("1", commandList.get(2).getParameter(0));
        Assert.assertEquals("2", commandList.get(2).getParameter(1));
        Assert.assertEquals("2", commandList.get(2).getParameter(2));
        Assert.assertEquals("99", commandList.get(2).getParameter(3));
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
        List<String> commands = interpreter.splitIntoCommands("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) hejsa 10)");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals("12 12", commandList.get(0).getParameter(0));
        Assert.assertEquals("35", commandList.get(0).getParameter(1));
        Assert.assertEquals("35 66", commandList.get(1).getParameter(0));
        Assert.assertEquals("2 2", commandList.get(1).getParameter(1));
        Assert.assertEquals("2 100", commandList.get(2).getParameter(0));
        Assert.assertEquals("hejsa", commandList.get(2).getParameter(1));
        Assert.assertEquals("10", commandList.get(2).getParameter(2));
    }

    @Test
    public void createCommands_TextAt(){
        List<String> commands = interpreter.splitIntoCommands("(TEXT-AT (12 12) Hejsa)");
        List<Command> commandList = interpreter.createCommands(commands);

        Assert.assertEquals("TEXT-AT", commandList.get(0).getName());
        Assert.assertEquals("12 12", commandList.get(0).getParameter(0));
        Assert.assertEquals("Hejsa", commandList.get(0).getParameter(1));

    }

    @Test
    public void createCommands_higherOrderFunctions_parameterCount(){
        List<String> commands = interpreter.splitIntoCommands("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals(2, commandList.get(0).getHigherOrderFunctions().size());
        Assert.assertEquals(2, commandList.get(1).getHigherOrderFunctions().size());
        Assert.assertEquals(1, commandList.get(1).getParameters().size());
    }

    @Test
    public void createCommands_higherOrderFunctions_parameterContent(){
        List<String> commands = interpreter.splitIntoCommands("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = interpreter.createCommands(commands);
        Assert.assertEquals("draw", commandList.get(0).getName());

        Assert.assertEquals("foo", commandList.get(0).getHigherOrderFunction(0).getName());
        Assert.assertEquals("12", commandList.get(0).getHigherOrderFunction(0).getParameter(0));
        Assert.assertEquals("13", commandList.get(0).getHigherOrderFunction(0).getParameter(1));

        Assert.assertEquals("LINE", commandList.get(0).getHigherOrderFunction(1).getName());
        Assert.assertEquals("2 2", commandList.get(0).getHigherOrderFunction(1).getParameter(0));
        Assert.assertEquals("22 2", commandList.get(0).getHigherOrderFunction(1).getParameter(1));

        Assert.assertEquals("FILL", commandList.get(1).getName());

        Assert.assertEquals("RED", commandList.get(1).getParameter(0));

        Assert.assertEquals("CIRCLE", commandList.get(1).getHigherOrderFunction(0).getName());
        Assert.assertEquals("35 66", commandList.get(1).getHigherOrderFunction(0).getParameter(0));
        Assert.assertEquals("5", commandList.get(1).getHigherOrderFunction(0).getParameter(1));

        Assert.assertEquals("LINE", commandList.get(1).getHigherOrderFunction(1).getName());
        Assert.assertEquals("2 2", commandList.get(1).getHigherOrderFunction(1).getParameter(0));
        Assert.assertEquals("5 5", commandList.get(1).getHigherOrderFunction(1).getParameter(1));
    }

    @Test
    public void parseColor_LotsOfStrings_TheColorOrBlackAsDefault(){
        List<String> colors = new ArrayList<>(Arrays.asList("RED", "green", "Cyan", "NoColor","Color.RED","","Evil","PurPlE", "blue", "pINK"));
        Assert.assertEquals(Color.RED, interpreter.parseColor(colors.get(0)));
        Assert.assertEquals(Color.GREEN, interpreter.parseColor(colors.get(1)));
        Assert.assertEquals(Color.cyan, interpreter.parseColor(colors.get(2)));
        Assert.assertEquals(Color.black, interpreter.parseColor(colors.get(3)));
        Assert.assertEquals(Color.black, interpreter.parseColor(colors.get(4)));
        Assert.assertEquals(Color.black, interpreter.parseColor(colors.get(5)));
        Assert.assertEquals(Color.black, interpreter.parseColor(colors.get(6)));
        Assert.assertEquals(Color.black, interpreter.parseColor(colors.get(7)));
        Assert.assertEquals(Color.BLUE, interpreter.parseColor(colors.get(8)));
        Assert.assertEquals(Color.PINK, interpreter.parseColor(colors.get(9)));
    }

    @Test
    public void interpret_correctPainterFunctionCalled(){
        interpreter.interpret("(LINE (2 2) (5 5))");
        verify(painter).drawLine(2,2,5,5);

        interpreter.interpret("(BOUNDING-BOX (1 1) (10 10))");
        verify(painter).setBoundingBox(1,1,10,10);

    }
}