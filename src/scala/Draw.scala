package scala

import java.awt.Color

import sample.Util.Constants.Scaling

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
    if (validateLineInput(x0, y0, x1, y1)) {
      BitmapOps.bresenham(rgbBitmap, x0, y0, x1, y1, c)
    } else {
      throw new IllegalArgumentException("Line values not valid")
    }
  }

  def gridLine(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    BitmapOps.bresenham(rgbBitmap, x0, y0, x1, y1, c, true)
  }

  def rectangle(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    if (validateRectangleInput(x0, y0, x1, y1)) {
      line(rgbBitmap, x0, y0, x0, y1, c)
      line(rgbBitmap, x0, y1, x1, y1, c)
      line(rgbBitmap, x1, y1, x1, y0, c)
      line(rgbBitmap, x0, y0, x1, y0, c)
    } else {
      throw new IllegalArgumentException("Rectangle values not valid")
    }
  }

  def rectangleFill(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    if (validateRectangleInput(x0, y0, x1, y1)) {
      var deltaY = y0 - y1
      var deltaYIsNegative = deltaY < 0
      var linesDrawn = 0

      while (linesDrawn <= math.abs(deltaY)) {
        if (deltaYIsNegative) {
          line(rgbBitmap, x0, y0 + linesDrawn, x1, y0 + linesDrawn, c)
        }
        else {
          line(rgbBitmap, x0, y0 - linesDrawn, x1, y0 - linesDrawn, c)
        }
        linesDrawn += 1
      }
    } else {
      throw new IllegalArgumentException("Rectangle values not valid")
    }
  }

  def circle(rgbBitmap: RgbBitmap, x: Int, y: Int, r: Int, c: Color) = {
    if (validateCircleInput(x, y, r)) {
//      BitmapOps.midpoint(rgbBitmap, x, y, r, c)
      BitmapOps.midpointRec(rgbBitmap, x, y, r, c)
    } else {
      throw new IllegalArgumentException("Circle values not valid")
    }
  }

  def circleFill(rgbBitmap: RgbBitmap, x: Int, y: Int, r: Int, c: Color) = {
    if (validateCircleInput(x, y, r)) {
//      BitmapOps.midpointFill(rgbBitmap, x, y, r, c)
      BitmapOps.midpointFillRec(rgbBitmap, x, y, r, c)
    } else {
      throw new IllegalArgumentException("Circle values not valid")
    }
  }

  def textAt(rgbBitmap: RgbBitmap, x: Int, y: Int, t: String, c: Color) = {
    rgbBitmap.setText(x, y, t, c)
  }

  def draw(rgbBitmap: RgbBitmap, c: Color, sl: ShapeList): RgbBitmap = sl match {
    case Nil() => rgbBitmap
    case Cons(hd, tail) => {
      draw(rgbBitmap, c, hd)
      draw(rgbBitmap, c, tail)
    }
  }

  def draw(rgbBitmap: RgbBitmap, c: Color, g: Shape) = g match {
    case Line(x0, y0, x1, y1) => line(rgbBitmap, x0* Scaling, Math.abs(y0 - Scaling) * Scaling, x1* Scaling, Math.abs(y1 - Scaling) * Scaling, c)
    case Rectangle(x0, y0, x1, y1) => rectangle(rgbBitmap, x0* Scaling, Math.abs(y0 - Scaling) * Scaling, x1* Scaling, Math.abs(y1 - Scaling) * Scaling, c)
    case Circle(x, y, r) => circle(rgbBitmap, x* Scaling, Math.abs(y - Scaling) * Scaling, r* Scaling, c)
    case TextAt(x, y, t) => textAt(rgbBitmap, x* Scaling, Math.abs(y - Scaling) * Scaling, t, c)
  }

  def fill(rgbBitmap: RgbBitmap, c: Color, g: Shape) = g match {
    case Rectangle(x0, y0, x1, y1) => rectangleFill(rgbBitmap, x0* Scaling, Math.abs(y0 - Scaling) * Scaling, x1* Scaling, Math.abs(y1 - Scaling) * Scaling, c)
    case Circle(x, y, r) => circleFill(rgbBitmap, x* Scaling, Math.abs(y - Scaling) * Scaling, r* Scaling, c)
  }

  def validateLineInput(x0: Int, y0: Int, x1: Int, y1: Int): Boolean = {
    Math.abs(x0 - x1) + Math.abs(y0 - y1) != 0
  }

  def validateRectangleInput(x0: Int, y0: Int, x1: Int, y1: Int): Boolean = {
    Math.abs(x0 - x1) != 0 && Math.abs(y0 - y1) != 0
  }

  def validateCircleInput(x: Int, y: Int, r: Int): Boolean = {
    r > 0
  }

}
