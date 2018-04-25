package scala

import java.awt.image.BufferedImage
import java.awt.{Color, Font}

import scala.annotation.tailrec

class RgbBitmap(val width: Int, val height: Int, val x0: Int, val y0: Int, val x1: Int, val y1: Int) {
  val image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)


  def fill(c: Color) = {
    val g = image.getGraphics()
    g.setColor(c)
    g.fillRect(0, 0, width, height)
  }

  def setPixel(x: Int, y: Int, c: Color) = {
    if (x >= x0 && x <= x1 && y >= y0 && y <= y1) {
      image.setRGB(x, y, c.getRGB())
    }
  }

  def validateCanvas(x: Int, y: Int): Boolean = {
    x >= 0 && y >= 0 && x < width && y < height
  }

  def setPixel(x: Int, y: Int, c: Color, prio: Boolean) = {
    if (validateCanvas(x, y)) {
      if (prio || validateBounds(x, y)) {
        image.setRGB(x, y, c.getRGB())
      }
    }
  }

  private def validateBounds(x: Int, y: Int): Boolean = {
    (x >= x0 && x <= x1 && y <= y0 && y >= y1)
  }

  def getPixel(x: Int, y: Int) = new Color(image.getRGB(x, y))

  def setText(x: Int, y: Int, t: String, c: Color) = {
    val g = image.getGraphics()
    g.setFont(new Font ("Arial Black", Font.BOLD, 10))
    g.setColor(c)
    g.drawString(t, x, y)
  }


  def pieChart(sl: List[Int]): Unit = {
    val g = image.getGraphics()

    // Sum from list of slices instead
    def sum(ints: List[Int]): Int = {
      @tailrec
      def sumAccumulator(ints: List[Int], accum: Int): Int = {
        ints match {
          case Nil => accum
          case x :: tail => sumAccumulator(tail, accum + x)
        }
      }
      sumAccumulator(ints, 0)
    }

    def sum2(xs: List[Int]): Int = {
      if (xs.isEmpty) 0
      else xs.head + sum2(xs.tail)
    }

    var total = 0
    var startAngle = 0
    var arcAngle = 0
    var curValue = 0
    var n = 0

    val slices = sl//List(25,25,25,25)
    val Colors = List[Color](
      Color.BLACK,
      Color.BLUE,
      Color.RED,
      Color.YELLOW,
      Color.CYAN,
      Color.GREEN)

    total = sum(slices)
    println("sum Total: " + total)
    total = 0
    println("reset Total: " + total)
    total = sum2(slices)
    println("sum2 Total: " + total)

    // Iterate through the list recursively?
    for (slice: Int <- slices) {
      drawChart(n, slice)
      n += 1 // Assignment bad (functional programming wise)?
    }

    def drawChart(n: Int, slice: Int) = {
      startAngle = curValue * 360 / total
      arcAngle = slice * 360 / total
      g.setColor(Colors(n))
      g.fillArc(100, 100, 50, 50, startAngle, arcAngle)
      curValue += slice
    }
  }
}