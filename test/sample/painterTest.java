package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sample.Controller;
import sample.Painter;
import sample.Util.Command;

import java.beans.Transient;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PainterTest {
    private Painter painter;
    private Controller uut;
    
    @Before
    public void setup() {
        uut = new Controller();

    }

    @Test
    public void drawStuff() {
        // How do we assert that draw is correct - Other than manual confirmation?
    }

    public void validateLineInput_correctInput_shouldReturnTrue() {
        // Call draw function with correct input
        // Assert that validate is TRUE   
        Assert.assertEquals(true, uut.validateLineInput(1, 1, 3, 3));
    }

    public void validateLineInput_incorrectInput_shouldReturnFalse() {
        // Assert that validate is FALSE with incorrect input (1,1,1,1) e.g.
        Assert.assertEquals(false, uut.validateLineInput(1, 1, 1, 1));
    }

    public void validateRectangleInput_correctInput_shouldReturnTrue() {
        // self explanatory
        Assert.assertEquals(true, uut.validateRectangleInput(2, 2, 4, 4));
    }

    public void validateRectangleInput_incorrectInput_shouldReturnFalse() {
        // Assert that validate is FALSE with incorrect input (1,1,1,1) e.g.
        // Check the math.Abs states
        Assert.assertEquals(false, uut.validateRectangleInput(2, 2, 2, 2));
    }




}