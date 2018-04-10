package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scala.BitmapOps;
import scala.RgbBitmap;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        RgbBitmap rgbBitmap = new RgbBitmap(300, 275);
        BitmapOps.midpoint(rgbBitmap, 150,150,50, Color.BLUE);
        Image image = SwingFXUtils.toFXImage(rgbBitmap.image(), null);
        VBox vBox = new VBox();
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        vBox.getChildren().addAll(imageView);

        Scene scene = new Scene(root, 300, 275);
        scene.setRoot(vBox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
