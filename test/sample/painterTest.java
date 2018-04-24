package sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class painterTest {
    private Painter painter;
    private Controller uut;
    
    @Before
    public void setup() {
        // Refactor to test Draw.scala validation functions instead
        uut = new Controller();

    }
    @Test
    public void validateLineInput_correctInput_shouldReturnTrue() {
        Assert.assertTrue(uut.validateLineInput(2,1,1,1));
        Assert.assertTrue(uut.validateLineInput(1,2,1,1));
        Assert.assertTrue(uut.validateLineInput(1,1,2,1));
        Assert.assertTrue(uut.validateLineInput(1,1,1,2));
    }
    @Test
    public void validateLineInput_incorrectInput_shouldReturnFalse() {
        Assert.assertFalse(uut.validateLineInput(1, 1, 1, 1));
    }
    @Test
    public void validateRectangleInput_correctInput_shouldReturnTrue() {
        Assert.assertTrue(uut.validateRectangleInput(5, 5, 4, 6));
        Assert.assertTrue(uut.validateRectangleInput(5, 5, 4, 4));
        Assert.assertTrue(uut.validateRectangleInput(5, 5, 6, 4));
        Assert.assertTrue(uut.validateRectangleInput(5, 5, 6, 6));
    }
    @Test
    public void validateRectangleInput_incorrectInput_shouldReturnFalse() {
        // Assert that validate is FALSE with incorrect input (1,1,1,1) e.g.
        // Check the math.Abs states
        Assert.assertFalse(uut.validateRectangleInput(5, 5, 5, 6));
        Assert.assertFalse(uut.validateRectangleInput(5, 5, 5, 4));
        Assert.assertFalse(uut.validateRectangleInput(5, 5, 4, 5));
        Assert.assertFalse(uut.validateRectangleInput(5, 5, 6, 5));
    }




}