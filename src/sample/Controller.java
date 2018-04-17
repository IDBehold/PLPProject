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

import static scala.Draw.*;

public class Controller implements Painter {
    @FXML
    VBox imageContainer;

    @FXML
    TextArea InputField;

    @FXML
    Label ErrorMessages;
    private Interpreter interpreter;

    public Controller() {
    }

    @FXML
    public void initialize() {
        interpreter = new Interpreter(this);

    }

    @FXML
    public void submit() {
        RgbBitmap rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), 50, 50, 350, 350);
        rgbBitmap.fill(Color.WHITE);
        Grid.draw(rgbBitmap, (int) imageContainer.getWidth(), (int) imageContainer.getHeight());
        rectangle(rgbBitmap,50,50,350,350, Color.BLACK);


        ErrorMessages.setText(Integer.toString((Interpreter.SplitInput(InputField.getText()).size())));
//        Cons shapeList = new Cons(new Line(100, 100, 200, 200), new Cons(new Line(0, 0, 150, 150), new Cons(new Rectangle(30,30,50,50), new Cons(new Rectangle(100,90, 75, 350), new Cons(new Rectangle(200,200,100,100), new Nil())))));
//        rgbBitmap = draw(rgbBitmap, Color.RED, shapeList);
//        circle(rgbBitmap, 150, 150, 100, Color.BLUE);
//        circle(rgbBitmap, 150, 150, 98, Color.BLUE);
//        circleFill(rgbBitmap, 150, 150, 75, Color.CYAN);
//        line(rgbBitmap, 50, 50, 100, 200, Color.CYAN);
//        rectangleFill(rgbBitmap,300,300,200,200, Color.BLACK);
//        rectangleFill(rgbBitmap,235,235,275,275, Color.GRAY);

        Slices sliceList = new Slices(new percentage(25), new Slices(new percentage(25), new Slices(new percentage(50), new Base())));
        drawPieChart(rgbBitmap,sliceList);
        textAt(rgbBitmap, 90, 90, "This is a pie chart", Color.BLACK);


        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageContainer.getChildren().addAll(imageView);
    }

    @Override
    public void setBoundingBox(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawRectangle(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawCircle(int x, int y, int r) {

    }

    @Override
    public void drawTextAt(int x, int y, String t) {

    }

    @Override
    public void drawShapes(Color c, List<Draw.Shape> shapes) {

    }

    @Override
    public void fillShape(Color c, Shape shape) {

    }
}
