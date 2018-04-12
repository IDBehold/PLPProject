package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.Draw;
import scala.RgbBitmap;

import java.awt.Color;

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
        RgbBitmap rgbBitmap = new RgbBitmap((int) imageContainer.getWidth(), (int) imageContainer.getHeight());
        rgbBitmap.fill(Color.WHITE);
        Draw.circle(rgbBitmap, 150, 150, 25, Color.BLUE);
        Draw.line(rgbBitmap,50, 50, 100, 100, Color.CYAN);
        Draw.rectangle(rgbBitmap, 20, 20, 50, 50, Color.RED);
        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageContainer.getChildren().addAll(imageView);
    }
}
