import scala.util.Random

object Tile {
  private var array: Array[Array[Tile]] = Array.ofDim(0)
  private var width: Int = -1
  private var height: Int = -1
  private var bombCount: Int = -1

  def getArray(): Array[Array[Tile]] = array
  def getArray(row: Int): Array[Tile] = array(row)
  def getBombsAround(x: Int, y: Int): Int = {
    var bombNum: Int = 0

    // check num of bombs left
    // left
    if (x > 0) {
      if (array(x-1)(y).isBomb()) bombNum += 1
      if (y > 0) if (array(x-1)(y-1).isBomb()) bombNum += 1
      if (y < height - 1) if (array(x-1)(y+1).isBomb()) bombNum += 1
    }

    // up/down
    if (y > 0) if (array(x)(y-1).isBomb()) bombNum += 1
    if (y < height - 1) if (array(x)(y+1).isBomb()) bombNum += 1

    // right
    if (x < width - 1) {
      if (array(x+1)(y).isBomb()) bombNum += 1
      if (y > 0) if (array(x+1)(y-1).isBomb()) bombNum += 1
      if (y < height - 1) if (array(x+1)(y+1).isBomb()) bombNum += 1
    }

    bombNum
  }
  def startupTiles(w: Int, h: Int, bombs: Int): Unit = {
    this.width = w
    this.height = h
    this.bombCount = bombs
    val bombPos: Array[Int] = Array.fill(bombs)(-1) // Array[Int] of size bombs filled with -1
    val numOfTiles: Int = width * height

    // random positions for bombs
    for(i <- bombPos.indices) {
      var found: Int = Random.nextInt(numOfTiles)
      while(bombPos.contains(found)) {
        found = Random.nextInt(numOfTiles)
      }
      bombPos(i) = found
    }

    // Array creation
    array = Array.ofDim(width, height)
    for(x <- array.indices) {
      for (y <- array(x).indices) {
        val isBomb: Boolean = bombPos.contains(width * y + x)
        array(x)(y) = new Tile(x,y,if (isBomb) Type.Bomb else Type.Empty)
      }
    }
  }
  /**
   * Returns remaining flags => Total bombs - used flags
   * @return Remaining flags
   */
  def getRemainingFlags(): Int = {
    var count = bombCount
    for (i <- array.indices) {
      for (j <- array(i).indices) {
        if (array(i)(j).isFlagged()) count -= 1
      }
    }
    count
  }
  /**
   * Used exclusively for Tile array debugging
   */
  def showArrayInConsole(): Unit = {
    var str: String = ""
    var bombCount: Int = 0

    for(i <- Tile.getArray().indices) {
      for (j <- Tile.getArray(i).indices) {
        if (Tile.getArray(i)(j).isBomb()) {
          str += "X"
          bombCount += 1
        }
        else {
          val around: Int = Tile.getBombsAround(i,j)
          if (around == 0) str += " "
          else str += around
        }
      }
      str += "\n"
    }
    println(str)
    println("Bombcount : " + bombCount)
  }
  def checkIfWon(): Boolean = {
    if (getRemainingFlags() != 0) return false

    // Are any tiles hidden ?
    for (i <- array.indices) {
      for (j <- array(i).indices) {
        if (!array(i)(j).isBomb() && array(i)(j).isHidden()) return false
      }
    }

    // All non-bomb tiles are not hidden + All flags used
    // Means that the player won the game
    true
  }
}

/**
 * Tile class used for flags, types and shown/hidden status for the GUI
 * @param x where it is placed (ARRAY POSITION NOT GUI COORDINATES)
 * @param y where it is placed (ARRAY POSITION NOT GUI COORDINATES)
 * @param typeOfTile Tile type found with *Type* object
 */
class Tile(val x: Int, val y: Int, typeOfTile: String) {
  private var hidden: Boolean = true  // All tiles are hidden by default
  private val tileType: String = typeOfTile // Tile type is given on construction
  private var flag: Boolean = false   // On startup, there are no flagged tiles

  def isHidden(): Boolean = hidden
  def isBomb(): Boolean = tileType == Type.Bomb
  def isFlagged(): Boolean = flag

  /**
   * If tile is flagged, remove it.
   * If tile isn't flagged, flag it
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
