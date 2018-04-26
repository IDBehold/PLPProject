package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.Draw;
import scala.Grid;
import scala.RgbBitmap;

import java.awt.Color;

import static sample.Util.Constants.Scaling;
import static scala.Draw.circle;
import static scala.Draw.draw;
import static scala.Draw.fill;
import static scala.Draw.line;
import static scala.Draw.rectangle;
import static scala.Draw.textAt;

public class Controller implements Painter {
    @FXML
    VBox imageContainer;

    @FXML
    TextArea InputField;

    @FXML
    Label ErrorMessages;
    private Interpreter interpreter;
    private RgbBitmap rgbBitmap;


    public Controller() {
    }

    @FXML
    public void initialize() {
        interpreter = new Interpreter(this);
        InputField.setText("(BOUNDING-BOX (1 1) (20 20))\n" +
                "(LINE (1 1) (1 20))\n" +
                "(LINE (1 1) (2 20))\n" +
                "(LINE (1 1) (3 20))\n" +
                "(LINE (1 1) (4 20))\n" +
                "(LINE (1 1) (5 20))\n" +
                "(LINE (1 1) (6 20))\n" +
                "(LINE (1 1) (7 20))\n" +
                "(LINE (1 1) (8 20))\n" +
                "(LINE (1 1) (9 20))\n" +
                "(LINE (1 1) (10 20))\n" +
                "(LINE (1 1) (11 20))\n" +
                "(LINE (1 1) (12 20))\n" +
                "(LINE (1 1) (13 20))\n" +
                "(LINE (1 1) (14 20))\n" +
                "(LINE (1 1) (15 20))\n" +
                "(LINE (1 1) (16 20))\n" +
                "(LINE (1 1) (17 20))\n" +
                "(LINE (1 1) (18 20))\n" +
                "(LINE (1 1) (19 20))\n" +
                "(LINE (1 1) (20 20))\n" +
                "(LINE (1 1) (20 19))\n" +
                "(LINE (1 1) (20 18))\n" +
                "(LINE (1 1) (20 17))\n" +
                "(LINE (1 1) (20 16))\n" +
                "(LINE (1 1) (20 15))\n" +
                "(LINE (1 1) (20 14))\n" +
                "(LINE (1 1) (20 13))\n" +
                "(LINE (1 1) (20 12))\n" +
                "(LINE (1 1) (20 11))\n" +
                "(LINE (1 1) (20 10))\n" +
                "(LINE (1 1) (20 9))\n" +
                "(LINE (1 1) (20 8))\n" +
                "(LINE (1 1) (20 7))\n" +
                "(LINE (1 1) (20 6))\n" +
                "(LINE (1 1) (20 5))\n" +
                "(LINE (1 1) (20 4))\n" +
                "(LINE (1 1) (20 3))\n" +
                "(LINE (1 1) (20 2))\n" +
                "(LINE (1 1) (20 1))");
    }

    @FXML
    public void submit() {

        ErrorMessages.setText("");
        try {
            interpreter.interpret(InputField.getText());
        } catch (Exception e) {
            ErrorMessages.setText(e.toString());
        }
//        Cons shapeList = new Cons(new Line(2, 2, 16, 16), new Cons(new Line(4, 4, 15, 15), new Cons(new Rectangle(3,3,5,5), new Cons(new Rectangle(10,9, 7, 3), new Cons(new Rectangle(1,1,1,1), new Nil())))));

        try{
            Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
            ImageView imageView = new ImageView();
            imageView.setImage(image);

            // Clear image container and add new imageView
            imageContainer.getChildren().clear();
            imageContainer.getChildren().addAll(imageView);
        } catch (Exception e) {
            ErrorMessages.setText("Failed to render image without a bounding-box");
        }
    }

    @Override
    public void setBoundingBox(int x0, int y0, int x1, int y1) {
        rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), x0*Scaling, Math.abs(y0-Scaling)*Scaling, x1*Scaling, Math.abs(y1-Scaling)*Scaling);
        rgbBitmap.fill(Color.WHITE);
        Grid.draw(rgbBitmap, (int) imageContainer.getWidth(), (int) imageContainer.getHeight());
        rectangle(rgbBitmap,x0*Scaling, Math.abs(y0-Scaling)*Scaling, x1*Scaling, Math.abs(y1-Scaling)*Scaling, Color.BLACK);
    }

    @Override
    public void drawLine(int x0, int y0, int x1, int y1) {
        line(rgbBitmap, x0 * Scaling, Math.abs(y0 - Scaling) * Scaling, x1 * Scaling, Math.abs(y1 - Scaling) * Scaling, Color.BLACK);
    }

    @Override
    public void drawRectangle(int x0, int y0, int x1, int y1) {
        rectangle(rgbBitmap, x0*Scaling, Math.abs(y0-Scaling)*Scaling, x1*Scaling, Math.abs(y1-Scaling)*Scaling, Color.BLACK);
    }

    @Override
    public void drawCircle(int x, int y, int r) {
        circle(rgbBitmap,x*Scaling, Math.abs(y-Scaling)*Scaling, r*Scaling, Color.BLACK);
    }

    @Override
    public void drawTextAt(int x, int y, String t) {
        textAt(rgbBitmap, x*Scaling, Math.abs(y-Scaling)*Scaling, t, Color.BLACK);
    }

    @Override
    public void drawShapes(Color c, Draw.ShapeList shapes) {
        draw(rgbBitmap, c, shapes);
    }

    @Override
    public void fillShape(Color c, Draw.Shape shape) {
        fill(rgbBitmap, c, shape);
    }


    // Redundant functions - Validation is happening in Draw.scala
    boolean validateLineInput(int x0, int y0, int x1, int y1) {
        return (Math.abs(x0 - x1) + Math.abs(y0 - y1) != 0);
    }
    boolean validateRectangleInput(int x0, int y0, int x1, int y1) {
        return ((Math.abs(x0 - x1) != 0 && (Math.abs(y0 - y1) != 0)));
    }
}
