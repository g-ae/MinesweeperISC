import scala.util.Random

object Tile {
  protected var array: Array[Array[Tile]] = Array.ofDim(0)

  def startupTiles(width: Int, height: Int, bombs: Int): Unit = {
    val bombPos: Array[Int] = Array.ofDim(bombs)
    val numOfTiles: Int = width * height

    // random positions for bombs
    for(i <- 0 until bombPos.indices) {
      var found: Int = Random.nextInt() * numOfTiles
      while(bombPos.contains(found)) {
        found = Random.nextInt() * numOfTiles
      }
      bombPos(i) = found
    }

    // Array creation
    array = Array.ofDim(width, height)
    for(x <- 0 until array.indices) {
      for (y <- 0 until array(x).indices) {
        // TODO : Should work but testing needed
        val isBomb: Boolean = bombPos.contains((width-1) * y + x)
        array(x)(y) = new Tile(x,y,if (isBomb) Types.Bomb else Types.Empty)
      }
    }
  }
}

class Tile(val x: Int, val y: Int, tileType: String) {
  protected var hidden: Boolean = true  // All tiles are hidden by default
  protected var tile: String = tileType // Tile type is given on construction
  protected var flag: Boolean = false   // On startup, there are no flagged tiles

  def isBomb(): Boolean = {
    tile == Types.Bomb
  }

  /**
   * If tile is flagged, remove it.
   * If Tile isn't flagged, flag it
   * @return new flagged state
   */
  def toggleFlag(): Boolean = {
    flag = !flag
    flag
  }

  /**
   * set "hidden" to false, so the user can see the interior
   * @return pressed bomb ? true=yes, false=no
   */
  def show(): Boolean = {
    hidden = false
    isBomb()
  }
}
