import hevs.graphics.FunGraphics

import java.awt.Color

object Minesweeper extends App {

  // Affichage lancement
  val screen = new FunGraphics(800, 460)
  screen.clear(Color.black)
  for(i <- 12 to 766 by 26
      ; j <- 60 to 424 by 26) {

    screen.setColor(Color.gray)
    screen.drawFillRect(i, j, 20, 20)
  }
  //fonction qui permet de générer une bordure autour d'une forme
  def bordure(x : Int, y : Int, xf : Int, yf : Int, esp : Int, inv : Int): Unit = {
    val wx = xf + 1 - x - esp ; val xesp = x + esp ; val x2 = xf + 1 - esp
    val wy = yf + 1 - y - esp ; val yesp = y + esp ; val y2 = yf + 1 - esp
    var dark: Color = if(inv == 0) Color.darkGray else Color.lightGray
    var light: Color = if(inv == 0) Color.lightGray else Color.darkGray
    screen.setColor(dark)
    screen.drawFillRect(x, y, wx, esp)
    screen.drawFillRect(x, y, esp, wy)
    screen.setColor(light)
    screen.drawFillRect(xesp, y2, wx, esp)
    screen.drawFillRect(x2, yesp, esp, wy)

    for(xi <- 0 until esp ;
        yi <- 0 until esp){
        if(yi <= ((esp-1) - xi)) screen.setPixel(x + xi, y2 + yi, dark) else screen.setPixel(x + xi, y2 + yi, light)

        if(yi > ((esp-1) - xi)) screen.setPixel(x2 + xi, y + yi, light) else screen.setPixel(x2 + xi, y + yi, dark)
    }
  }
  //Bordure principale
  bordure(3, 51, 794, 452, 6, 0)

  for(x <- 9 to 763 by 26;
    y <- 57 to 421 by 26){
    bordure(x, y, x + 25, y + 25, 3, 1)
  }

}