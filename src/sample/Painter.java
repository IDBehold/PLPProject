package sample;

import scala.Draw;

import java.awt.*;
import java.util.List;

public interface Painter {

    void setBoundingBox(int x1, int y1, int x2, int y2);

    void drawLine(int x1, int y1, int x2, int y2);
    void drawRectangle(int x1, int y1, int x2, int y2);
    void drawCircle(int x, int y, int r);
    void drawTextAt(int x, int y, String t);

    void drawShapes(Color c, List<Draw.Shape> shapes);
    void fillShape(Color c, Shape shape);

}
