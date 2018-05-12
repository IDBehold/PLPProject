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
import scala.ScalaInterpreter;

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
    private RgbBitmap rgbBitmap;


    public Controller() {
    }

    @FXML
    public void initialize() {
        InputField.setText("(bounding-box (2 2) (18 18))\n" +
                "(circle (10 10) 7)\n" +
                "(line (10 10) (17 10))\n" +
                "(line (10 10) (15 15))\n" +
                "(line (10 10) (10 17))\n" +
                "(line (10 10) (5 5))\n" +
                "(line (10 10) (15 5))\n" +
                "\n" +
                "(text-at (11 15) 12.5%)\n" +
                "(text-at (14 12) 12.5%)\n" +
                "(text-at (13 9) 12.5%)\n" +
                "(text-at (10 6) 25%)\n" +
                "(text-at (5 11) 37.5%)\n" +
                "\n" +
                "(text-at (2 1) 2)\n" +
                "(text-at (4 1) 4)\n" +
                "(text-at (6 1) 6)\n" +
                "(text-at (8 1) 8)\n" +
                "(text-at (10 1) 10)\n" +
                "(text-at (12 1) 12)\n" +
                "(text-at (14 1) 14)\n" +
                "(text-at (16 1) 16)\n" +
                "(text-at (18 1) 18)\n" +
                "\n" +
                "(text-at (1 2) 2)\n" +
                "(text-at (1 4) 4)\n" +
                "(text-at (1 6) 6)\n" +
                "(text-at (1 8) 8)\n" +
                "(text-at (1 10) 10)\n" +
                "(text-at (1 12) 12)\n" +
                "(text-at (1 14) 14)\n" +
                "(text-at (1 16) 16)\n" +
                "(text-at (1 18) 18)");
    }

    @FXML
    public void submit() {

        imageContainer.getChildren().clear();
        ErrorMessages.setText("");
        rgbBitmap = null;
        try {
            ScalaInterpreter.interpret(InputField.getText(), this);

            // Hardcoded shapes for test purpose
            drawLine(10, 10, 15, 17);
            drawCircle(6,8,3);

            Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
            ImageView imageView = new ImageView();
            imageView.setImage(image);

            // Clear image container and add new imageView
            imageContainer.getChildren().clear();
            imageContainer.getChildren().addAll(imageView);
        } catch (Exception e) {
            ErrorMessages.setText(e.toString());
        }
    }

    @Override
    public void setBoundingBox(int x0, int y0, int x1, int y1) {
        if(x0 > x1)
        {
            int newx0 = x1;
            int newx1 = x0;
            x0 = newx0;
            x1 = newx1;
        }
        if(y0 > y1)
        {
            int newy0 = y1;
            int newy1 = y0;
            y0 = newy0;
            y1 = newy1;
        }
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
