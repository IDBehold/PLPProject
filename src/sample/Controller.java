package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.Draw;
import scala.Draw.Rectangle;
import scala.Grid;
import scala.RgbBitmap;

import java.awt.Color;

import static scala.Draw.Cons;
import static scala.Draw.Line;
import static scala.Draw.Nil;
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

    }

    @FXML
    public void submit() {
//        setBoundingBox(2,2,18,18); // Always first in input field
        interpreter.interpret(InputField.getText());

//        ErrorMessages.setText(Integer.toString((interpreter.splitIntoCommands(InputField.getText()).size())));
//        Cons shapeList = new Cons(new Line(2, 2, 16, 16), new Cons(new Line(4, 4, 15, 15), new Cons(new Rectangle(3,3,5,5), new Cons(new Rectangle(10,9, 7, 3), new Cons(new Rectangle(1,1,1,1), new Nil())))));
//
//        drawRectangle(1,1,1,1);
//        fillShape(Color.GREEN, new Rectangle(5,5,15,15));
//
//        drawShapes(Color.BLACK, shapeList);
//
//        drawCircle(10,10,0);
//        drawCircle(10,10,-10);

        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        // Clear image container and add new imageView
        imageContainer.getChildren().clear();
        imageContainer.getChildren().addAll(imageView);
    }

    @Override
    public void setBoundingBox(int x0, int y0, int x1, int y1) {
        rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), x0*20, Math.abs(y0-20)*20, x1*20, Math.abs(y1-20)*20);
        rgbBitmap.fill(Color.WHITE);
        Grid.draw(rgbBitmap, (int) imageContainer.getWidth(), (int) imageContainer.getHeight());
        rectangle(rgbBitmap,x0*20, Math.abs(y0-20)*20, x1*20, Math.abs(y1-20)*20, Color.BLACK);
    }

    @Override
    public void drawLine(int x0, int y0, int x1, int y1) {
        line(rgbBitmap, x0 * 20, Math.abs(y0 - 20) * 20, x1 * 20, Math.abs(y1 - 20) * 20, Color.BLACK);
    }

    @Override
    public void drawRectangle(int x0, int y0, int x1, int y1) {
        rectangle(rgbBitmap, x0*20, Math.abs(y0-20)*20, x1*20, Math.abs(y1-20)*20, Color.BLACK);
    }

    @Override
    public void drawCircle(int x, int y, int r) {
        circle(rgbBitmap,x*20, Math.abs(y-20)*20, r*20, Color.BLACK);
    }

    @Override
    public void drawTextAt(int x, int y, String t) {
        textAt(rgbBitmap, x*20, Math.abs(y-20)*20, t, Color.BLACK);
    }

    @Override
    public void drawShapes(Color c, Draw.ShapeList shapes) {
        draw(rgbBitmap, Color.BLACK, shapes);
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
