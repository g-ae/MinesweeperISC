
import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{MouseAdapter, MouseEvent}

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
    // Mouse listener -> listens for mouse clicks
    screen.addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent): Unit = mouseClickInGame(e)
    })
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
  /**
   * Show real square
   * @param x array index X
   * @param y array index Y
   */
  def unHideSquare(x: Int, y: Int): Unit = {
    val realX: Int = getRealXFromArrayIndex(x)
    val realY: Int = getRealYFromArrayIndex(y)
    Window.screen.setColor(Color.gray)
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
    println("drew flag at ",x,y)
    screen.setColor(Color.red)
    screen.drawFillRect(getRealXFromArrayIndex(x)+5,getRealYFromArrayIndex(y)+5,10,10)
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
    screen.drawString(x + 6, y + 20, str, getColorFromNumber(number), 24)
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
    if (!Minesweeper.isGameRunning) return

    val tileCoords: Array[Int] = getTileFromCoords(e.getX, e.getY)
    val tileX: Int = tileCoords(0)
    val tileY: Int = tileCoords(1)

    // e.getButton -> LMB = 1, RMB = 3
    e.getButton match {
      case 1 =>
        val clickResult = Tile.getArray(tileX)(tileY).leftclick(true)
        if (!clickResult) Minesweeper.endGame(false)
      case 3 =>
        Tile.getArray(tileX)(tileY).rightclick()
    }
    // check for win
    if (Tile.checkIfWon()) Minesweeper.endGame(true)
  }
  def getColorFromNumber(number: Int): Color = {
    number match {
      case 1 => Color.blue
      case 2 => Color.green
      case 3 => Color.red
      case 4 => new Color(0,0,120)
      case 5 => new Color(120,0,0)
      case 6 => new Color(0,140,140)
      case 7 => Color.black
      case 8 => Color.darkGray
      case _ => Color.black
    }
  }
  def drawTextTop(text: String): Unit = {
    screen.drawString(10, 45, text, Color.white, 32)
  }
}
