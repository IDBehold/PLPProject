package scala

import java.awt.Color

object Grid {

  def horizontal(rgbBitmap: RgbBitmap, x0: Int, y0: Int, xn: Int, yn: Int, n: Int): Unit = {
    if (y0 < yn) {
      Draw.gridLine(rgbBitmap, x0, y0, xn, y0, Color.lightGray)
      horizontal(rgbBitmap, x0, y0 + (yn / n), xn, yn, n)
    }
  }

  def vertical(rgbBitmap: RgbBitmap, x0: Int, y0: Int, xn: Int, yn: Int, n: Int): Unit = {
    if (x0 < xn) {
      Draw.gridLine(rgbBitmap, x0, y0, x0, yn, Color.lightGray)
      vertical(rgbBitmap, x0 + (xn / n), y0, xn, yn, n)
    }
  }

  def draw(rgbBitmap: RgbBitmap, width: Int, height: Int) = {
    horizontal(rgbBitmap, 0, 0, width, height, 20)
    vertical(rgbBitmap, 0, 0, width, height, 20)
  }

}
