package scala

import java.awt.Color

object Draw {

  abstract class Shape

  case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Shape

  case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Shape

  case class Circle(x: Int, y: Int, r: Int) extends Shape

  case class TextAt(x: Int, y: Int, t: String) extends Shape

  case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Shape


  def line(x1: Int, y1: Int, x2: Int, y2: Int, c: Color) = {

  }

  def rectangle(x1: Int, y1: Int, x2: Int, y2: Int, c: Color) = {

  }

  def circle(x: Int, y: Int, r: Int, c: Color) = {

  }

  def textAt(x: Int, y: Int, t: String, c: Color) = {

  }

  def boundingBox(x1: Int, y1: Int, x2: Int, y2: Int, c: Color) = {

  }

  def draw(c: Color, g: Shape) = g match { //g should be a list of shapes
    case Line(x1, y1, x2, y2) => line(x1, y1, x2, y2, c)
    case Rectangle(x1, y1, x2, y2) => rectangle(x1, y1, x2, y2, c)
    case Circle(x, y, r) => circle(x, y, r, c)
    case TextAt(x, y, t) => textAt(x, y, t, c)
    case BoundingBox(x1, y1, x2, y2) => boundingBox(x1, y1, x2, y2, c)

  }

  def fill(c: Color, g: Shape) = {
    //TBD
  }
}
