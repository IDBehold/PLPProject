package scala

import java.awt.image.BufferedImage
import java.awt.{Color, Font}

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
    (x >= x0 && x <= x1 && y >= y0 && y <= y1)
  }

  def getPixel(x: Int, y: Int) = new Color(image.getRGB(x, y))

  def setText(x: Int, y: Int, t: String, c: Color) = {
    val g = image.getGraphics()
    g.setFont(new Font ("Arial Black", Font.BOLD, 12))
    g.setColor(c)
    g.drawString(t, x, y)
  }


  def pieChart(): Unit = {
    val g = image.getGraphics()
    val total = 100
    var startAngle = 0
    var arcAngle = 0
    var curValue = 0

    val slices = List(25,25,25,25)
    val Colors = List[Color](Color.BLACK, Color.BLUE, Color.RED, Color.YELLOW)
    for (slice: Int <- slices) {
      var n = 0
      drawChart(n, slice)
      n+1
    }

    def drawChart(n: Int, slice: Int) = {
      startAngle = curValue * 360 / total
      arcAngle = (slice * 360 / total)
      g.setColor(Colors(n))
      g.fillArc(100, 100, 50, 50, startAngle, arcAngle)
      curValue += slices(n)
    }

  }
}