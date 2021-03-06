package sample;

import scala.Draw;

import java.awt.Color;

public interface Painter {

    void setBoundingBox(int x1, int y1, int x2, int y2);

    void drawLine(int x1, int y1, int x2, int y2);
    void drawRectangle(int x1, int y1, int x2, int y2);
    void drawCircle(int x, int y, int r);
    void drawTextAt(int x, int y, String t);

    void drawShapes(Color c, Draw.ShapeList shapes);
    void fillShape(Color c, Draw.Shape shape);

}
