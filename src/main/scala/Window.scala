
import hevs.graphics.FunGraphics

import java.awt.Color

object Window {
  //Par defaut 800/ 460
  private var screen : FunGraphics = _
  var carrex = 30   //Default number of square on x
  var carrey = 15   //Default number of square on y

  /**
   * Fonction that init the window
   * @param x width of the window
   * @param y heigh of the window
   */
  def createScreen(x : Int, y : Int): Unit = {
    screen = new FunGraphics(x , y)
    Window.screen.clear(Color.black)
  }

  def createSquares(): Unit ={
    var x = ((carrex - 1)* 26) + 12 ; var y = ((carrey-1)* 26)+ 60
    for(i <- 12 to x by 26;
        j <- 60 to y by 26){
      Window.screen.setColor(Color.gray)
      screen.drawFillRect(i , j, 20 , 20)
    }

    var xi = ((carrex - 1)* 26) + 9 ; var yi = ((carrey-1)* 26)+ 57
    for(x <- 9 to xi by 26;
        y <- 57 to yi by 26){
      createBorder(x, y, x + 25, y + 25, 3, 1)
    }
  }

  def createBigBorder(x : Int, y: Int, esp: Int, inv : Int): Unit = createBorder(x, y,(carrex * 26) + x + 11, (carrey * 26) + y + 11, esp, inv)


  def createBorder(x : Int, y : Int, xf : Int, yf : Int, esp : Int, inv : Int): Unit = {
    val wx = xf + 1 - x - esp ; val xesp = x + esp ; val x2 = xf + 1 - esp
    val wy = yf + 1 - y - esp ; val yesp = y + esp ; val y2 = yf + 1 - esp
    var dark: Color = if(inv == 0) Color.darkGray else Color.lightGray
    var light: Color = if(inv == 0) Color.lightGray else Color.darkGray
    Window.screen.setColor(dark)
    Window.screen.drawFillRect(x, y, wx, esp)
    Window.screen.drawFillRect(x, y, esp, wy)
    Window.screen.setColor(light)
    Window.screen.drawFillRect(xesp, y2, wx, esp)
    Window.screen.drawFillRect(x2, yesp, esp, wy)

    for(xi <- 0 until esp ;
        yi <- 0 until esp){
      if(yi <= ((esp-1) - xi)) Window.screen.setPixel(x + xi, y2 + yi, dark) else Window.screen.setPixel(x + xi, y2 + yi, light)

      if(yi > ((esp-1) - xi)) Window.screen.setPixel(x2 + xi, y + yi, light) else Window.screen.setPixel(x2 + xi, y + yi, dark)
    }
  }

  def getTileFromCoords(x : Int, y : Int): Array[Int] = {
    var extx : Int = (26 * carrex) + 9
    var exty : Int = (26 * carrey) + 57
    var dx = 0 ; var dy = 0
    var x2 = x - 9 ; var y2 = y - 57

    // valeur de x dans le tableau
    if (8 < x  && x < extx) {
      var restex = x2 % 26
      dx = ((x2 - restex) / 26)
    }
    else dx = -1

    // valeur de y dans le tableau
    if (56 < y && y < exty){
      var restey = y2 % 26
      dy = ((y2 - restey)/ 26)
    }
    else dy = -1

    Array(dx , dy)


  }


}
