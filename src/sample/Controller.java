package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import scala.BitmapOps;
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
        BitmapOps.midpoint(rgbBitmap, 150, 150, 50, Color.BLUE);
        BitmapOps.bresenham(rgbBitmap, 100, 100, 0, 0, Color.RED);
        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageContainer.getChildren().addAll(imageView);
    }
}
