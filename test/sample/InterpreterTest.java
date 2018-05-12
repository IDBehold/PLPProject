package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Util.ColorUtils;
import sample.Util.Command;
import scala.Draw;
import scala.ScalaInterpreter;
import sun.plugin2.util.ColorUtil;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InterpreterTest {
    private Painter painter;

    @Before
    public void setup() {
        painter = mock(Painter.class);
    }

    @Test
    public void splitIntoCommands_correctAmountOfCommands() {
        Assert.assertEquals(1, ScalaInterpreter.splitIntoCommands("(s)").size());
        Assert.assertEquals(2, ScalaInterpreter.splitIntoCommands("(s)\n(test (1 2) 3)").size());
        Assert.assertEquals(3, ScalaInterpreter.splitIntoCommands("    (s)\n(te)(s)").size());
        Assert.assertEquals(4, ScalaInterpreter.splitIntoCommands("(s)\n(s)\n(s)\n(s)").size());
    }

    @Test
    public void splitIntoCommands_correctContent_outerParenthesesRemoved() {
        Assert.assertEquals("s", ScalaInterpreter.splitIntoCommands("(s)").get(0));
        Assert.assertEquals("test (1 2) 3", ScalaInterpreter.splitIntoCommands("(s)\n(test (1 2) 3)").get(1));
        Assert.assertEquals("s", ScalaInterpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(0));
        Assert.assertEquals("test 1 2 3", ScalaInterpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(1));
        Assert.assertEquals("Z", ScalaInterpreter.splitIntoCommands("    (s)\n(test 1 2 3)(Z)").get(2));
        Assert.assertEquals("22", ScalaInterpreter.splitIntoCommands("(s)\n(s)\n(s)\n(22)").get(3));
    }

    @Test
    public void createCommands_correctCommandNames() {
//        List<String> commandStrings = ScalaInterpreter.splitIntoCommands("(function (12 12) 35)\n(test (1 2) (2 99) text)(foo 1 2 3)(foo 1 2 3)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(function (12 12) 35)\n(test (1 2) (2 99) text)(foo 1 2 3)(foo 1 2 3)");
        Assert.assertEquals("function", commandList.get(0).getName());
        Assert.assertEquals("test", commandList.get(1).getName());
        Assert.assertEquals("foo", commandList.get(2).getName());
        Assert.assertEquals("foo", commandList.get(3).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void createCommands_commandNameWithSpecialCharacters_invalidCommandName() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(function!.( 1212)35)");
        ScalaInterpreter.createCommandsTest("(function!.( 1212)35)");
    }

    @Test
    public void createCommands_correctCommandParameterCount_IntegerParameters() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
        Assert.assertEquals(3, commandList.get(0).getParameters().size());
        Assert.assertEquals(2, commandList.get(1).getParameters().size());
        Assert.assertEquals(4, commandList.get(2).getParameters().size());
    }

    @Test
    public void createCommands_correctCommandParameter_IntegerParameters() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(function 12 13 35) (foo 35 66)\n(test 1 2 2 99)");
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
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) test 10)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) test 10)");
        Assert.assertEquals(2, commandList.get(0).getParameters().size());
        Assert.assertEquals(2, commandList.get(1).getParameters().size());
        Assert.assertEquals(3, commandList.get(2).getParameters().size());
    }

    @Test
    public void createCommands_correctCommandParameter() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) hejsa 10)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(function (12 12) 35) (foo (35 66) (2 2))\n(test (2 100) hejsa 10)");
        Assert.assertEquals("12 12", commandList.get(0).getParameter(0));
        Assert.assertEquals("35", commandList.get(0).getParameter(1));
        Assert.assertEquals("35 66", commandList.get(1).getParameter(0));
        Assert.assertEquals("2 2", commandList.get(1).getParameter(1));
        Assert.assertEquals("2 100", commandList.get(2).getParameter(0));
        Assert.assertEquals("hejsa", commandList.get(2).getParameter(1));
        Assert.assertEquals("10", commandList.get(2).getParameter(2));
    }

    @Test
    public void createCommands_TextAt() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(TEXT-AT (12 12) Hejsa)");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(TEXT-AT (12 12) Hejsa)");

        Assert.assertEquals("TEXT-AT", commandList.get(0).getName());
        Assert.assertEquals("12 12", commandList.get(0).getParameter(0));
        Assert.assertEquals("Hejsa", commandList.get(0).getParameter(1));

    }

    @Test
    public void createCommandsTest_higherOrderFunctions_parameterCount() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        Assert.assertEquals(2, commandList.get(0).getHigherOrderFunctions().size());
        Assert.assertEquals(2, commandList.get(1).getHigherOrderFunctions().size());
        Assert.assertEquals(1, commandList.get(1).getParameters().size());
    }

    @Test
    public void createCommandsTest_higherOrderFunctions_parameterContent() {
//        List<String> commands = ScalaInterpreter.splitIntoCommands("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
        List<Command> commandList = ScalaInterpreter.createCommandsTest("(draw (foo 12 13) (LINE (2 2) (22 2)))\n\t(FILL RED (CIRCLE (35 66) 5) (LINE (2 2) (5 5)))");
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
    public void parseColor_LotsOfStrings_TheColorOrBlackAsDefault() {
        List<String> colors = new ArrayList<>(Arrays.asList("RED", "green", "Cyan", "NoColor", "Color.RED", "", "Evil", "PurPlE", "blue", "pINK"));
        Assert.assertEquals(Color.RED, ColorUtils.parseColor(colors.get(0)));
        Assert.assertEquals(Color.GREEN, ColorUtils.parseColor(colors.get(1)));
        Assert.assertEquals(Color.cyan, ColorUtils.parseColor(colors.get(2)));
        Assert.assertEquals(Color.black, ColorUtils.parseColor(colors.get(3)));
        Assert.assertEquals(Color.black, ColorUtils.parseColor(colors.get(4)));
        Assert.assertEquals(Color.black, ColorUtils.parseColor(colors.get(5)));
        Assert.assertEquals(Color.black, ColorUtils.parseColor(colors.get(6)));
        Assert.assertEquals(Color.black, ColorUtils.parseColor(colors.get(7)));
        Assert.assertEquals(Color.BLUE, ColorUtils.parseColor(colors.get(8)));
        Assert.assertEquals(Color.PINK, ColorUtils.parseColor(colors.get(9)));
    }

    @Test
    public void interpret_correctPainterFunctionCalled() {
        String boundingBox = "(BOUNDING-BOX (1 1) (10 10))\n";
        ScalaInterpreter.interpret(boundingBox, painter);
        verify(painter).setBoundingBox(1, 1, 10, 10);

        ScalaInterpreter.interpret(boundingBox + "(LINE (2 2) (5 5))", painter);
        verify(painter).drawLine(2, 2, 5, 5);

        ScalaInterpreter.interpret(boundingBox + "(RECTANGLE (2 2) (5 8))", painter);
        verify(painter).drawRectangle(2, 2, 5, 8);

        ScalaInterpreter.interpret(boundingBox + "(CIRCLE (2 22) 5)", painter);
        verify(painter).drawCircle(2, 22, 5);

        ScalaInterpreter.interpret(boundingBox + "(TEXT-AT (20 20) test12.5%)", painter);
        verify(painter).drawTextAt(20, 20, "test12.5%");
    }

    @Test
    public void interpret_fillCommand_painterCalledCorrect() {
        String boundingBox = "(BOUNDING-BOX (1 1) (10 10))\n";
        ScalaInterpreter.interpret(boundingBox + "(FILL RED (RECTANGLE (1 2) (5 5)))", painter);
        verify(painter).fillShape(Color.RED, new Draw.Rectangle(1, 2, 5, 5));
        ScalaInterpreter.interpret(boundingBox + "(FILL RED (CIRCLE (1 2) 5))", painter);
        verify(painter).fillShape(Color.RED, new Draw.Circle(1, 2, 5));
    }

    @Test
    public void interpret_drawCommand_painterCalledCorrect() {
        String boundingBox = "(BOUNDING-BOX (1 1) (10 10))\n";
        ScalaInterpreter.interpret(boundingBox + "(DRAW RED (RECTANGLE (1 2) (5 5)))", painter);
        verify(painter).drawShapes(Color.RED, new Draw.Cons(new Draw.Rectangle(1, 2, 5, 5), new Draw.Nil()));

        ScalaInterpreter.interpret(boundingBox + "(DRAW GREEN (RECTANGLE (1 2) (5 5)) (LINE (5 5) (10 10)) (CIRCLE (6 6) 16))", painter);
        verify(painter).drawShapes(Color.GREEN, new Draw.Cons(new Draw.Circle(6, 6, 16), new Draw.Cons(new Draw.Line(5, 5, 10, 10), new Draw.Cons(new Draw.Rectangle(1, 2, 5, 5), new Draw.Nil()))));

        ScalaInterpreter.interpret(boundingBox + "(DRAW BLUE (TEXT-AT (5 5) Hejsa) (RECTANGLE (1 2) (5 5)) (LINE (5 5) (10 10)) (CIRCLE (6 6) 16))", painter);
        verify(painter).drawShapes(Color.BLUE, new Draw.Cons(new Draw.Circle(6, 6, 16), new Draw.Cons(new Draw.Line(5, 5, 10, 10), new Draw.Cons(new Draw.Rectangle(1, 2, 5, 5), new Draw.Cons(new Draw.TextAt(5,5,"Hejsa"), new Draw.Nil())))));
    }

    @Test(expected = InvalidParameterException.class)
    public void interpret_tooManyParenthesis_InvalidParameterException() {
        ScalaInterpreter.interpret("(BOUNDING-BOX (1 1) (10 10))(LINE (5 5) (10 10)))", painter);
    }

    @Test(expected = InvalidParameterException.class)
    public void interpret_tooFewParenthesis_InvalidParameterException() {
        ScalaInterpreter.interpret("(BOUNDING-BOX (1 1) (10 10))(LINE (5 5) (10 10)", painter);
    }

    @Test(expected = InvalidParameterException.class)
    public void interpret_inputFieldIsEmpty_InvalidParameterException() {
        ScalaInterpreter.interpret("                                             ", painter);
    }

    @Test(expected = NoSuchElementException.class)
    public void interpret_nonsense_IndexOutOfBoundsException() {
        ScalaInterpreter.interpret("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", painter);
    }
}