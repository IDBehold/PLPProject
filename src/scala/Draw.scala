package scala

import java.awt.Color
import scala.BitmapOps

object Draw {

  abstract class Shape

  case class Line(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape

  case class Rectangle(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape

  case class Circle(x: Int, y: Int, r: Int) extends Shape

  case class TextAt(x: Int, y: Int, t: String) extends Shape

  case class BoundingBox(x0: Int, y0: Int, x1: Int, y1: Int) extends Shape


  def line(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    BitmapOps.bresenham(rgbBitmap, x0, y0, x1, y1, c)
  }

  def rectangle(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {
    
  }

  def circle(rgbBitmap: RgbBitmap, x: Int, y: Int, r: Int, c: Color) = {
    BitmapOps.midpoint(rgbBitmap, x, y, r, c)
  }

  def textAt(rgbBitmap: RgbBitmap, x: Int, y: Int, t: String, c: Color) = {

  }

  def boundingBox(rgbBitmap: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color) = {

  }

  def draw(rgbBitmap: RgbBitmap, c: Color, g: Shape) = g match { //g should be a list of shapes
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