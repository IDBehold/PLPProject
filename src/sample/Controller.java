package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.Grid;
import scala.RgbBitmap;

import java.awt.Color;

import static scala.Draw.Cons;
import static scala.Draw.Line;
import static scala.Draw.Nil;
import static scala.Draw.Rectangle;
import static scala.Draw.circle;
import static scala.Draw.circleFill;
import static scala.Draw.draw;
import static scala.Draw.line;
import static scala.Draw.rectangle;
import static scala.Draw.rectangleFill;
import static scala.Draw.textAt;

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
            RgbBitmap rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight(), 50, 50, 350, 350);
            rgbBitmap.fill(Color.WHITE);
            Grid.draw(rgbBitmap, (int) imageContainer.getWidth(), (int) imageContainer.getHeight());


            Cons shapeList = new Cons(new Line(100, 100, 200, 200), new Cons(new Line(0, 0, 150, 150), new Cons(new Rectangle(30,30,50,50), new Cons(new Rectangle(100,90, 75, 350), new Cons(new Rectangle(200,200,100,100), new Nil())))));
            rgbBitmap = draw(rgbBitmap, Color.RED, shapeList);
            circle(rgbBitmap, 150, 150, 100, Color.BLUE);
            circle(rgbBitmap, 150, 150, 98, Color.BLUE);
            circleFill(rgbBitmap, 150, 150, 75, Color.CYAN);
            line(rgbBitmap, 50, 50, 100, 200, Color.CYAN);
            rectangle(rgbBitmap,50,50,350,350, Color.BLACK);
            rectangleFill(rgbBitmap,300,300,200,200, Color.BLACK);
            rectangleFill(rgbBitmap,235,235,275,275, Color.GRAY);
            textAt(rgbBitmap, 25, 25, "This is a test", Color.RED);
            textAt(rgbBitmap, 100, 100, "Second test", Color.BLACK);



            Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
            ImageView imageView = new ImageView();
            imageView.setImage(image);

            imageContainer.getChildren().addAll(imageView);
    }
}
