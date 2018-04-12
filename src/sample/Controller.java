package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.Draw;
import scala.RgbBitmap;

import java.awt.Color;

import static scala.Draw.*;

public class Controller {
    @FXML
    VBox imageContainer;

    public Controller() {
    }

    @FXML
    public void initialize() {


    }

    @FXML
    public void submit() {
        RgbBitmap rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), 50,50, 300,300);
        rgbBitmap.fill(Color.WHITE);


        Cons shapeList = new Cons(new Line(100, 100, 200, 200), new Cons(new Line(0, 0, 150, 150), new Cons(new Rectangle(30,30,50,50), new Cons(new Rectangle(100,90, 75, 350), new Cons(new Rectangle(200,200,100,100), new Nil())))));
        RgbBitmap lines = draw(rgbBitmap, Color.RED, shapeList);
        circle(lines, 150, 150, 100, Color.BLUE);
        circle(lines, 150, 150, 98, Color.BLUE);
        circleFill(lines, 150, 150, 75, Color.CYAN);
        line(lines, 50, 50, 100, 200, Color.CYAN);
        rectangle(lines,50,50,300,300, Color.BLACK);

        Image image = SwingFXUtils.toFXImage(lines.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageContainer.getChildren().addAll(imageView);
    }
}
