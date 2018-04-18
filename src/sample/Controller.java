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


import java.awt.*;
import java.awt.Shape;

import static scala.Draw.*;

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
        setBoundingBox(2,2,18,18); // Always first in input field

//        drawLine(5,5,5,5); // Validate input - This will crash
        drawLine(2,2,4,4);
        drawLine(2,18,4,16);
        drawRectangle(4,4,12,16);
        drawLine(12,4, 18,2);
        drawLine(12,16,18,18);


        ErrorMessages.setText(Integer.toString((interpreter.splitIntoCommands(InputField.getText()).size())));
//        Cons shapeList = new Cons(new Line(100, 100, 200, 200), new Cons(new Line(0, 0, 150, 150), new Cons(new Rectangle(30,30,50,50), new Cons(new Rectangle(100,90, 75, 350), new Cons(new Rectangle(200,200,100,100), new Nil())))));
//        circleFill(rgbBitmap, 150, 150, 75, Color.CYAN);
//
//        Slices sliceList = new Slices(new percentage(25), new Slices(new percentage(25), new Slices(new percentage(50), new Base())));
//        drawPieChart(rgbBitmap,sliceList);


        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageContainer.getChildren().addAll(imageView);
    }

    @Override
    public void setBoundingBox(int x0, int y0, int x1, int y1) {
        rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), x0*20, y0*20, x1*20, y1*20);
        rgbBitmap.fill(Color.WHITE);
        Grid.draw(rgbBitmap, (int) imageContainer.getWidth(), (int) imageContainer.getHeight());
        rectangle(rgbBitmap,x0*20, y0*20, x1*20, y1*20, Color.BLACK);
    }

    @Override
    public void drawLine(int x0, int y0, int x1, int y1) {
        line(rgbBitmap, x0*20, Math.abs(y0-20)*20, x1*20, Math.abs(y1-20)*20, Color.BLACK);
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
    public void drawShapes(Color c, java.util.List<Draw.Shape> shapes) {

    }

    @Override
    public void fillShape(Color c, Shape shape) {

    }
}
