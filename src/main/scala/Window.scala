
import hevs.graphics.FunGraphics

import java.awt.Color
import java.awt.event.{MouseAdapter, MouseEvent, MouseListener}

object Window {
  //Par defaut 800/ 460
  private var screen : FunGraphics = _
  private val backgroundColor: Color = Color.white
  private val foregroundColor: Color = Color.black
  val inGameAdapter: MouseAdapter = new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = mouseClickInGame(e)
  }
  val menuAdapter: MouseAdapter = new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = mouseClickMenu(e)
  }
  val gameEndedAdapter: MouseAdapter = new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = mouseClickGameEnded(e)
  }

  var carrex = 30   //Default number of square on x
  var carrey = 15   //Default number of square on y

  /**
   * Function that init the window
   * @param x width of the window
   * @param y heigh of the window
   */
  def createScreen(x : Int, y : Int): Unit = {
    screen = new FunGraphics(x, y, "MinesweeperISC")
    Window.screen.clear(Window.backgroundColor)
  }

  def clearScreen(): Unit = {
    screen.clear(backgroundColor)
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
    val realX: Int = getRealXFromArrayIndex(x)
    val realY: Int = getRealYFromArrayIndex(y)

    screen.setColor(Color.red)
    //the flag
    screen.drawFillRect(realX + 12, realY + 4, 3, 2)
    screen.drawFillRect(realX + 12, realY + 11, 3, 2)
    screen.drawFillRect(realX + 9, realY + 6, 6, 2)
    screen.drawFillRect(realX + 9, realY + 9, 6, 2)
    screen.drawFillRect(realX + 7, realY + 8, 8, 1)
    // The stick
    screen.setColor(Color.black)
    screen.drawFillRect(realX + 13, realY + 13, 2, 3)
    screen.drawFillRect(realX + 9, realY + 16, 8, 2)
    screen.drawFillRect(realX + 7, realY + 18, 12, 4)

    drawRemainingFlags()
  }

  /**
   * Flag count top of screen
   */
  def drawRemainingFlags(): Unit = {
    screen.setColor(Window.backgroundColor)
    screen.drawFillRect(299, 0, 150, 50)
    screen.drawString(300, 45, Tile.getRemainingFlags().toString, Color.red, 32)
  }

  /**
   * Remove flag
   * @param x array index X
   * @param y array index Y
   */
  def removeFlag(x: Int, y: Int): Unit = {
    screen.setColor(Color.gray)
    screen.drawFillRect(getRealXFromArrayIndex(x)+4,getRealYFromArrayIndex(y)+4,18,18)

    drawRemainingFlags()
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
    if (number == -1) {
      drawBomb(x,y)
      return
    }
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
    val tileCoords: Array[Int] = getTileFromCoords(e.getX, e.getY)
    val tileX: Int = tileCoords(0)
    val tileY: Int = tileCoords(1)

    // If the click is outside the playable area, don't do anything
    if (tileX == -1 || tileY == -1) return

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
    screen.drawString(10, 45, text, foregroundColor, 32)
  }

  /**
   * Clicks all bombs (used on game lost)
   */
  def showAllBombs(): Unit = {
    for(i <- Tile.getArray.indices) {
      for (tile <- Tile.getArray(i)) {
        if (tile.isBomb()) tile.leftclick()
      }
    }
  }

  def showMenu(): Unit = {
    screen.clear(Window.backgroundColor)
    screen.drawString(160, 70, "MinesweeperISC", Window.foregroundColor, 58)
    screen.mainFrame.getContentPane.addMouseListener(Window.menuAdapter)

    // check mouseClickMenu function for menu clicks
    // BUTTON: Beginner X: 100 to 250, Y: 250 to 350
    Window.drawButton(100, 250, 150, 100, "Beginner\n10x10\n10 bombs")

    // BUTTON: Medium X: 320 - 470, Y: 250 to 350
    Window.drawButton(320, 250, 150, 100, "Medium\n20x15\n45 bombs")

    // BUTTON: Hard X: 540 - 690, Y: 250 to 350
    Window.drawButton(540, 250, 150, 100, "Hard\n30x15\n90 bombs")
  }

  def drawButton(x: Int, y: Int, width: Int, height: Int, text: String): Unit = {
    // Button border
    screen.setColor(Window.foregroundColor)
    screen.drawFillRect(x, y, width, height)

    // Inside of button (gray)
    screen.setColor(Color.lightGray)
    screen.drawFillRect(x+3, y+3, width - 6, height - 6)

    var i: Int = 1
    for (t <- text.split("\n")) {
      screen.drawString(x+6, y+i*24, t, Window.foregroundColor, 24)
      i+=1
    }
  }

  def drawBomb(x : Int, y : Int): Unit ={
    //base of the bomb
    screen.setColor(Color.black)
    screen.drawFillRect(x + 7, y + 13, 7, 10)
    screen.drawFillRect(x + 6, y + 14, 9, 8)
    screen.drawFillRect(x + 5, y + 15, 11, 6)
    screen.drawFillRect(x + 9, y + 11, 3, 2)
    //fuse of the bomb
    screen.drawFillRect(x + 10, y + 9, 1,2)
    screen.setPixel(x + 11, y + 8)
    screen.drawFillRect(x + 12, y + 7, 2,1)
    screen.drawFillRect(x + 18, y + 7, 3,1)
    screen.setPixel(x + 16, y + 4)
    screen.setPixel(x + 17, y + 3)
    screen.setPixel(x + 16, y + 10)
    screen.setPixel(x + 17, y + 11)
    //reflect of the bomb
    screen.setColor(Color.gray)
    screen.drawFillRect(x + 6, y + 18, 1, 2)
    screen.setPixel(x + 7, y + 20)
    screen.setPixel(x + 8, y + 21)
    screen.setPixel(x + 11, y + 14)
    screen.setPixel(x + 12, y + 15)
    screen.drawFillRect(x + 13, y + 15, 1, 2)
    screen.drawFillRect(x + 14, y + 16, 1, 4)
    screen.setPixel(x + 20, y + 13)
    //flame of the bomb
    screen.setColor(Color.orange)
    screen.drawFillRect(x + 14, y + 6, 2, 3)
    screen.setColor(Color.red)
    screen.setPixel(x + 13, y + 6)
    screen.setPixel(x + 14, y + 5)
    screen.setPixel(x + 15, y + 6)
    screen.setPixel(x + 16, y + 7)
    screen.setPixel(x + 15, y + 8)
    screen.setPixel(x + 14, y + 9)
    screen.setPixel(x + 13, y + 8)
    screen.setPixel(x + 14, y + 7, Color.yellow)
  }

  def mouseClickMenu(e: MouseEvent): Unit = {
    // If outside button height borders, do nothing
    if (e.getY < 250 || e.getY > 350) return

    if (e.getX >= 100 && e.getX <= 250) {
      // BEGINNER 10x10, 10 bombs
      screen.mainFrame.getContentPane.removeMouseListener(Window.menuAdapter)
      Minesweeper.startGame(10,10,10)
      screen.mainFrame.getContentPane.addMouseListener(Window.inGameAdapter)
    } else if (e.getX >= 320 && e.getX <= 470) {
      // MEDIUM 20x15, 45 bombs
      screen.mainFrame.getContentPane.removeMouseListener(Window.menuAdapter)
      Minesweeper.startGame(20,15,45)
      screen.mainFrame.getContentPane.addMouseListener(Window.inGameAdapter)
    } else if (e.getX >= 540 && e.getX <= 690) {
      // HARD
      screen.mainFrame.getContentPane.removeMouseListener(Window.menuAdapter)
      Minesweeper.startGame(30,15,90)
      screen.mainFrame.getContentPane.addMouseListener(Window.inGameAdapter)
    }
  }

  /**
   * Remove in-game clicks and add new button
   */
  def gameEnded(): Unit = {
    screen.mainFrame.getContentPane.removeMouseListener(Window.inGameAdapter)
    Window.drawButton(700,10,90,40,"Replay")
    screen.mainFrame.getContentPane.addMouseListener(Window.gameEndedAdapter)
  }

  def mouseClickGameEnded(e: MouseEvent): Unit = {
    if (e.getX < 700 || e.getX > 790 || e.getY < 10 || e.getY > 50) return

    screen.mainFrame.getContentPane.removeMouseListener(Window.gameEndedAdapter)
    Window.showMenu()
  }
}
