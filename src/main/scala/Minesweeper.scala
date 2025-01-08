import hevs.graphics.FunGraphics

import java.awt.Color

object Minesweeper extends App {

  Window.carrex = 30
  Window.carrey = 15
  Window.createScreen(800, 460)
  Window.createSquares()
  Window.createBigBorder(3, 51, 6, 0)
}
