
import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{MouseAdapter, MouseEvent}

object Window {
  //Par defaut 800/ 460
  private var screen : FunGraphics = _
  var carrex = 30   //Default number of square on x
  var carrey = 15   //Default number of square on y

  /**
   * Function that init the window
   * @param x width of the window
   * @param y heigh of the window
   */
  def createScreen(x : Int, y : Int): Unit = {
    screen = new FunGraphics(x , y)
    // Mouse listener -> listens for mouse clicks
    screen.addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent): Unit = mouseClickInGame(e)
    })
    Window.screen.clear(Color.black)
  }

  /**
   * Function that create all the tiles
   */
  def createSquares(): Unit ={
    var x = ((carrex - 1)* 26) + 12 ; var y = ((carrey-1)* 26)+ 60

    // creates the inside of the tile
    for(i <- 12 to x by 26;
        j <- 60 to y by 26){
      Window.screen.setColor(Color.gray)
      screen.drawFillRect(i , j, 20 , 20)
    }

    // creates the borders of the tile
    var xi = ((carrex - 1)* 26) + 9 ; var yi = ((carrey-1)* 26)+ 57
    for(x <- 9 to xi by 26;
        y <- 57 to yi by 26){
      createBorder(x, y, x + 25, y + 25, 3, 1)
    }
  }

  /**
   * Create the Main border of the little tiles
   * @param x value of the pixel on the top right (x)
   * @param y value of the pixel on the top right (y)
   * @param esp the width of the border
   * @param inv 0 for the up/left border darkGray and else for lightGray
   */
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
  /**
   * Show real square
   * @param x array index X
   * @param y array index Y
   */
  def unHideSquare(x: Int, y: Int): Unit = {
    val realX: Int = getRealXFromArrayIndex(x)
    val realY: Int = getRealYFromArrayIndex(y)
    Window.screen.setColor(new Color(75,75,75))
    Window.screen.drawFillRect(realX, realY, 26, 26)
    showNumAt(realX, realY, Tile.getBombsAround(x,y))
    Window.createBorder(realX, realY, realX + 25, realY + 25, 1, 0)
  }
  /**
   * Insert flag
   * @param x array index X
   * @param y array index Y
   */
  def drawFlag(x : Int, y: Int): Unit = {
    // TODO : Change flag visual style (currently a red rectangle)
    screen.setColor(Color.red)
    //the flag
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 12,getRealYFromArrayIndex(y)+4,3,2)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 12,getRealYFromArrayIndex(y)+11,3,2)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 9,getRealYFromArrayIndex(y)+6,6,2)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 9,getRealYFromArrayIndex(y)+9,6,2)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 7,getRealYFromArrayIndex(y)+8,8,1)
    // The stick
    screen.setColor(Color.black)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 13,getRealYFromArrayIndex(y)+13,2,3)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 9,getRealYFromArrayIndex(y)+16,8,2)
    screen.drawFillRect(getRealXFromArrayIndex(x)+ 7,getRealYFromArrayIndex(y)+18,12,4)
  }

  /**
   * Remove flag
   * @param x array index X
   * @param y array index Y
   */
  def removeFlag(x: Int, y: Int): Unit = {
    screen.setColor(Color.gray)
    screen.drawFillRect(getRealXFromArrayIndex(x)+5,getRealYFromArrayIndex(y)+5,15,15)
  }
  /**
   * Insert specified number at coordinates
   * @param x X coordinate
   * @param y Y coordinate
   * @param number number to be shown. 0 will display nothing, -1 will display X (BOMB)
   */
  def showNumAt(x:Int, y:Int, number: Int): Unit = {
    if (number == 0) return
    var str: String = number.toString
    if (number == -1) str = "X"
    screen.drawString(x + 6, y + 20, str, Color.white, 24)
  }

  def getRealXFromArrayIndex(indexX: Int): Int = indexX * 26 + 9
  def getRealYFromArrayIndex(indexY: Int): Int = indexY * 26 + 57

  def getTileFromCoords(x : Int, y : Int): Array[Int] = {
    var extx : Int = (26 * carrex) + 9
    var exty : Int = (26 * carrey) + 57
    var dx = 0 ; var dy = 0
    var x2 = x - 9 ; var y2 = y - 57

    // valeur de x dans le tableau
    if (8 < x  && x < extx) {
      var restex = x2 % 26
      dx = (x2 - restex) / 26
    }
    else dx = -1

    // valeur de y dans le tableau
    if (56 < y && y < exty){
      var restey = y2 % 26
      dy = (y2 - restey)/ 26
    }
    else dy = -1

    Array(dx , dy)
  }

  /**
   * Event used for every mouse click (in-game)
   * @param e MouseEvent
   */
  def mouseClickInGame(e: MouseEvent): Unit = {
    val tileCoords: Array[Int] = getTileFromCoords(e.getX, e.getY)
    val tileX: Int = tileCoords(0)
    val tileY: Int = tileCoords(1)

    // e.getButton -> LMB = 1, RMB = 3
    e.getButton match {
      case 1 =>
        val clickResult = Tile.getArray(tileX)(tileY).leftclick()
        if (!clickResult) {
          println("You lost the game.")
          // TODO : Prevent user from continuing pressing buttons (game is over)
        }
      case 3 =>
        Tile.getArray(tileX)(tileY).rightclick()
    }
    // check for win
    if (Tile.checkIfWon()) {
      println("You won the game !")
      // TODO : Prevent user from continuing pressing buttons (game is over)
    }
  }

  def draw_bomb(x : Int, y : Int): Unit ={
    var realx = getRealXFromArrayIndex(x) ; var realy = getRealYFromArrayIndex(y)
    //base of the bomb
    screen.setColor(Color.black)
    screen.drawFillRect(realx + 7, realy + 13, 7, 10)
    screen.drawFillRect(realx + 6, realy + 14, 9, 8)
    screen.drawFillRect(realx + 5, realy + 15, 11, 6)
    screen.drawFillRect(realx + 9, realy + 11, 3, 2)
    //fuse of the bomb
    screen.drawFillRect(realx + 10, realy + 9, 1,2)
    screen.setPixel(realx + 11, realy + 8)
    screen.drawFillRect(realx + 12, realy + 7, 2,1)
    screen.drawFillRect(realx + 18, realy + 7, 3,1)
    screen.setPixel(realx + 16, realy + 4)
    screen.setPixel(realx + 17, realy + 3)
    screen.setPixel(realx + 16, realy + 10)
    screen.setPixel(realx + 17, realy + 11)
    //reflect of the bomb
    screen.setColor(Color.gray)
    screen.drawFillRect(realx + 6, realy + 18, 1, 2)
    screen.setPixel(realx + 7, realy + 20)
    screen.setPixel(realx + 8, realy + 21)
    screen.setPixel(realx + 11, realy + 14)
    screen.setPixel(realx + 12, realy + 15)
    screen.drawFillRect(realx + 13, realy + 15, 1, 2)
    screen.drawFillRect(realx + 14, realy + 16, 1, 4)
    screen.setPixel(realx + 20, realy + 13)
    //flame of the bomb
    screen.setColor(Color.orange)
    screen.drawFillRect(realx + 14, realy + 6, 2, 3)
    screen.setColor(Color.red)
    screen.setPixel(realx + 13, realy + 6)
    screen.setPixel(realx + 14, realy + 5)
    screen.setPixel(realx + 15, realy + 6)
    screen.setPixel(realx + 16, realy + 7)
    screen.setPixel(realx + 15, realy + 8)
    screen.setPixel(realx + 14, realy + 9)
    screen.setPixel(realx + 13, realy + 8)
    screen.setPixel(realx + 14, realy + 7, Color.yellow)
  }


}
