object Minesweeper {
  private var gameRunning = true

  def isGameRunning: Boolean = gameRunning
  def startGame(width: Int, height: Int, bombs: Int): Unit = {
    gameRunning = true

    // Tile array and bomb setup
    Window.carrex = width
    Window.carrey = height
    Tile.startupTiles(width, height, bombs)

    // Display game on user's screen
    Window.createSquares()
    Window.createBigBorder(3, 51, 6, 0)
  }

  def endGame(win: Boolean): Unit = {
    Window.drawTextTop(if (win) "You won !" else "You lost")
    Window.showAllBombs()
    gameRunning = false
  }

  def main(args: Array[String]): Unit = {
    // Display game on user's screen
    Window.createScreen(800, 460)

    // Default : 30, 15, 99
    startGame(30, 15, 99)
  }
}
