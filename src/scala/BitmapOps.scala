package scala

import java.awt.Color

object BitmapOps {
  def midpoint(bm:RgbBitmap, x0:Int, y0:Int, radius:Int, c:Color)={
    var f=1-radius
    var ddF_x=1
    var ddF_y= -2*radius
    var x=0
    var y=radius

    bm.setPixel(x0, y0+radius, c)
    bm.setPixel(x0, y0-radius, c)
    bm.setPixel(x0+radius, y0, c)
    bm.setPixel(x0-radius, y0, c)

    while(x < y)
    {
      if(f >= 0)
      {
        y-=1
        ddF_y+=2
        f+=ddF_y
      }
      x+=1
      ddF_x+=2
      f+=ddF_x

      bm.setPixel(x0+x, y0+y, c)
      bm.setPixel(x0-x, y0+y, c)
      bm.setPixel(x0+x, y0-y, c)
      bm.setPixel(x0-x, y0-y, c)
      bm.setPixel(x0+y, y0+x, c)
      bm.setPixel(x0-y, y0+x, c)
      bm.setPixel(x0+y, y0-x, c)
      bm.setPixel(x0-y, y0-x, c)
    }
  }

  def bresenham(bm:RgbBitmap, x0:Int, y0:Int, x1:Int, y1:Int, c:Color)={
        val dx=math.abs(x1-x0)
        val sx=if (x0<x1) 1 else -1
        val dy=math.abs(y1-y0)
        val sy=if (y0<y1) 1 else -1

        def it=new Iterator[Tuple2[Int,Int]]{
           var x=x0; var y=y0
           var err=(if (dx>dy) dx else -dy)/2
           def next={
              val res=(x,y)
              val e2=err;
              if (e2 > -dx) {err-=dy; x+=sx}
              if (e2<dy) {err+=dx; y+=sy}
              res;
           }
           def hasNext = (sx*x <= sx*x1 && sy*y <= sy*y1)
        }

        for((x,y) <- it)
           bm.setPixel(x, y, c)
     }
}
