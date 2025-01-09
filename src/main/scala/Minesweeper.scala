object Minesweeper extends App {
  Window.carrex = 30
  Window.carrey = 15

  // Tile array and bomb setup
  Tile.startupTiles(Window.carrex, Window.carrey, 50)

  // Display game on user's screen
  Window.createScreen(800, 460)
  Window.createSquares()
  Window.createBigBorder(3, 51, 6, 0)
}
