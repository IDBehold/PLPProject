package scala

import java.awt.Color

object Draw {
  sealed abstract class ShapeList
  case class Cons(head:Shape, tail: ShapeList) extends ShapeList
  case class Nil() extends ShapeList

  sealed abstract class SliceList
  case class Slices(head: Slice, tail: SliceList) extends SliceList
  case class Base() extends SliceList

  abstract class Slice
  case class percentage(p: Int) extends Slice

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

  def drawPieChart(rgbBitmap: RgbBitmap, sl: SliceList) = {
    var list = List(25,25,25,25)
    listSlices(sl, list)
    rgbBitmap.pieChart(list)
  }

  //
  def listSlices(sl: SliceList, list: List[Int]): List[Int] = sl match {
    case Base() => return list
    case Slices(hd, tail) => {
      println("From listSlices recursive call: " + hd)
      getSlice(hd, list)
      // Add value to list and call pieChart in RgbBitmap?
      // how do I add the value from percentage parameters to a list?
      listSlices(tail, list)
    }
  }

  def getSlice(sl: Slice, list: List[Int]): List[Int] = sl match {
    case percentage(p) =>
      println(p)
      list.+:(p)
      return list
    // percentage value retrieval?
  }

  def Add(list: List[Int], p: Int): List[Int] = {
    list :+ p
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
  }

  def fill(c: Color, g: Shape) = {
    //TBD
  }


}
