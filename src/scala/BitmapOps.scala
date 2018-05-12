package scala

import java.awt.Color

import scala.annotation.tailrec

object BitmapOps {
  def midpoint(bm: RgbBitmap, x0: Int, y0: Int, radius: Int, c: Color) = {
    var f = 1 - radius
    var ddF_x = 1
    var ddF_y = -2 * radius
    var x = 0
    var y = radius

    bm.setPixel(x0, y0 + radius, c)
    bm.setPixel(x0, y0 - radius, c)
    bm.setPixel(x0 + radius, y0, c)
    bm.setPixel(x0 - radius, y0, c)

    while (x < y) {
      if (f >= 0) {
        y -= 1
        ddF_y += 2
        f += ddF_y
      }
      x += 1
      ddF_x += 2
      f += ddF_x

      bm.setPixel(x0 + x, y0 + y, c)
      bm.setPixel(x0 - x, y0 + y, c)
      bm.setPixel(x0 + x, y0 - y, c)
      bm.setPixel(x0 - x, y0 - y, c)
      bm.setPixel(x0 + y, y0 + x, c)
      bm.setPixel(x0 - y, y0 + x, c)
      bm.setPixel(x0 + y, y0 - x, c)
      bm.setPixel(x0 - y, y0 - x, c)
    }
  }

  def midpointRec(bm: RgbBitmap, x0: Int, y0: Int, radius: Int, c: Color): Unit = {
    var f = 1 - radius
    var ddF_x = 1
    var ddF_y = -2 * radius
    var x = 0
    var y = radius
    bm.setPixel(x0, y0 + radius, c)
    bm.setPixel(x0, y0 - radius, c)
    bm.setPixel(x0 + radius, y0, c)
    bm.setPixel(x0 - radius, y0, c)
    setPixelsRecursive()

    @tailrec def setPixelsRecursive(): Unit =
      if (x < y) {
        if (f >= 0) {
          y -= 1
          ddF_y += 2
          f += ddF_y
        }
        x += 1
        ddF_x += 2
        f += ddF_x

        bm.setPixel(x0 + x, y0 + y, c)
        bm.setPixel(x0 - x, y0 + y, c)
        bm.setPixel(x0 + x, y0 - y, c)
        bm.setPixel(x0 - x, y0 - y, c)
        bm.setPixel(x0 + y, y0 + x, c)
        bm.setPixel(x0 - y, y0 + x, c)
        bm.setPixel(x0 + y, y0 - x, c)
        bm.setPixel(x0 - y, y0 - x, c)
        setPixelsRecursive()
      }
  }

  def midpointFill(bm: RgbBitmap, x0: Int, y0: Int, radius: Int, c: Color) = {
    var f = 1 - radius
    var ddF_x = 1
    var ddF_y = -2 * radius
    var x = 0
    var y = radius

    bm.setPixel(x0, y0 + radius, c)
    bm.setPixel(x0, y0 - radius, c)
    bm.setPixel(x0 + radius, y0, c)
    bm.setPixel(x0 - radius, y0, c)

    while (x < y) {
      if (f >= 0) {
        y -= 1
        ddF_y += 2
        f += ddF_y
      }
      x += 1
      ddF_x += 2
      f += ddF_x

      bresenham(bm, x0 + x, y0 + y, x0 - x, y0 + y, c)
      bresenham(bm, x0 + x, y0 - y, x0 - x, y0 - y, c)
      bresenham(bm, x0 + y, y0 + x, x0 - y, y0 + x, c)
      bresenham(bm, x0 + y, y0 - x, x0 - y, y0 - x, c)
    }
    bresenham(bm, x0 - radius, y0, x0 + radius, y0, c)
  }

  def midpointFillRec(bm: RgbBitmap, x0: Int, y0: Int, radius: Int, c: Color) = {
    var f = 1 - radius
    var ddF_x = 1
    var ddF_y = -2 * radius
    var x = 0
    var y = radius

    bm.setPixel(x0, y0 + radius, c)
    bm.setPixel(x0, y0 - radius, c)
    bm.setPixel(x0 + radius, y0, c)
    bm.setPixel(x0 - radius, y0, c)

    setPixelsRecursive()

    @tailrec def setPixelsRecursive(): Unit =
      if (x < y) {
        if (f >= 0) {
          y -= 1
          ddF_y += 2
          f += ddF_y
        }
        x += 1
        ddF_x += 2
        f += ddF_x

        bresenham(bm, x0 + x, y0 + y, x0 - x, y0 + y, c)
        bresenham(bm, x0 + x, y0 - y, x0 - x, y0 - y, c)
        bresenham(bm, x0 + y, y0 + x, x0 - y, y0 + x, c)
        bresenham(bm, x0 + y, y0 - x, x0 - y, y0 - x, c)
        setPixelsRecursive()
      }

    bresenham(bm, x0 - radius, y0, x0 + radius, y0, c)
  }


  def bresenham(bitmap: RgbBitmap, i: Int, i1: Int, i2: Int, i3: Int, color: Color): Unit = {
//    bresenham(bitmap, i, i1, i2, i3, color, false)
    bresenhamRec(bitmap, i, i1, i2, i3, color, false)
  }
  def bresenham(bm: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color, prio: Boolean): Unit = {
    val dx = math.abs(x1 - x0)
    val sx = if (x0 < x1) 1 else -1
    val dy = math.abs(y1 - y0)
    val sy = if (y0 < y1) 1 else -1

    def it = new Iterator[Tuple2[Int, Int]] {
      var x: Int = x0
      var y: Int = y0
      var err: Int = (if (dx > dy) dx else -dy) / 2

      def next: (Int, Int) = {
        val res = (x, y)
        val e2 = err
        if (e2 > -dx) {
          err -= dy
          x += sx
        }
        if (e2 < dy) {
          err += dx
          y += sy
        }
        res
      }

      def hasNext: Boolean = sx * x <= sx * x1 && sy * y <= sy * y1
    }

    for ((x, y) <- it)
      bm.setPixel(x, y, c, prio)
  }

  def bresenhamRec(bm: RgbBitmap, x0: Int, y0: Int, x1: Int, y1: Int, c: Color, prio: Boolean): Unit = {
    val dx = math.abs(x1 - x0)
    val sx = if (x0 < x1) 1 else -1
    val dy = math.abs(y1 - y0)
    val sy = if (y0 < y1) 1 else -1
    step(x0, y0)

    @tailrec def step(x: Int, y: Int): Unit = {
        var x_step: Int = x
        var y_step: Int = y
        var err: Int = (if (dx > dy) dx else -dy) / 2
        val e2 = err
        if (sx * x_step <= sx * x1 && sy * y_step <= sy * y1) {
          if (e2 > -dx) {
            err -= dy
            x_step += sx
          }
          if (e2 < dy) {
            err += dx
            y_step += sy
          }
          bm.setPixel(x_step, y_step, c, prio)
          step(x_step, y_step)
        }
    }
  }
}
