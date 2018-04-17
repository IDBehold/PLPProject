package scala

import java.awt.Color

object Draw {
  sealed abstract class ShapeList
  case class Cons(head:Shape, tail: ShapeList) extends ShapeList
  case class Nil() extends ShapeList

  abstract class Shape
  case class Line(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape
  case class Rectangle(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape
  case class Circle(x: Int, y: Int, r: Int) extends Shape
  case class TextAt(x: Int, y: Int, t: String) extends Shape
  case class BoundingBox(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape

  def line(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    BitmapOps.bresenham(rgbBitmap, x0, y0, x1, y1, c)
  }

  def gridLine(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    BitmapOps.bresenham(rgbBitmap, x0, y0, x1, y1, c, true)
  }

  def rectangle(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    line(rgbBitmap, x0,y0,x0,y1,c)
    line(rgbBitmap, x0,y1,x1,y1,c)
    line(rgbBitmap, x1,y1,x1,y0,c)
    line(rgbBitmap, x0,y0,x1,y0,c)
  }

  def rectangleFill(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    var deltaY = y0 - y1
    var deltaYIsNegative = deltaY < 0
    var linesDrawn = 0

    while(linesDrawn <= math.abs(deltaY)) {
        if(deltaYIsNegative) {
          line(rgbBitmap, x0,y0 + linesDrawn,x1,y0 + linesDrawn,c)
          }
        else{
          line(rgbBitmap, x0,y0 - linesDrawn,x1,y0 - linesDrawn,c)
        }
        linesDrawn += 1
      }
  }

  def circle(rgbBitmap: RgbBitmap, x: Int, y: Int, r: Int, c: Color) = {
    BitmapOps.midpoint(rgbBitmap, x, y, r, c)
  }

  def circleFill(rgbBitmap: RgbBitmap, x: Int, y: Int, r: Int, c: Color) = {
    BitmapOps.midpointFill(rgbBitmap, x, y, r, c)
  }

  def textAt(rgbBitmap: RgbBitmap, x: Int, y: Int, t: String, c: Color) = {
    rgbBitmap.setText(x, y, t, c)
  }

  def piecChart(rgbBitmap: RgbBitmap) = {
    rgbBitmap.pieChart()
  }

  def boundingBox(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {

  }

  def draw(rgbBitmap: RgbBitmap, c: Color, sl: ShapeList): RgbBitmap = sl match {
    case Nil() => rgbBitmap
    case Cons(hd, tail) => {
      draw(rgbBitmap, c, hd)
      draw(rgbBitmap, c, tail)
    }
  }

  def draw(rgbBitmap: RgbBitmap, c: Color, g: Shape) = g match {
    case Line(x0, y0, x1, y1) => line(rgbBitmap, x0, y0, x1, y1, c)
    case Rectangle(x0, y0, x1, y1) => rectangle(rgbBitmap, x0, y0, x1, y1, c)
    case Circle(x, y, r) => circle(rgbBitmap, x, y, r, c)
    case TextAt(x, y, t) => textAt(rgbBitmap, x, y, t, c)
    case BoundingBox(x0, y0, x1, y1) => boundingBox(rgbBitmap, x0, y0, x1, y1, c)

  }

  def fill(c: Color, g: Shape) = {
    //TBD
  }


}
