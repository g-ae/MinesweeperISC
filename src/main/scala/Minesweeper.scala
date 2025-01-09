object Minesweeper {
  def startGame(width: Int, height: Int, bombs: Int): Unit = {
    // Tile array and bomb setup
    Window.carrex = width
    Window.carrey = height
    Tile.startupTiles(bombs)

    // Display game on user's screen
    Window.clearScreen()
    Window.createSquares()
    Window.createBigBorder(3, 51, 6, 0)
    Window.drawRemainingFlags()
  }

  def endGame(win: Boolean): Unit = {
    Window.drawTextTop(if (win) "You won !" else "You lost")
    Window.showAllBombs()
    Window.gameEnded()
  }

  def main(args: Array[String]): Unit = {
    // Display game on user's screen
    Window.createScreen(800, 460)

    Window.showMenu()
  }
}
