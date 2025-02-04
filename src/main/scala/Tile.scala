import AroundType.AroundType
import TileType.TileType
import scala.util.Random

/**
 * Enum used for getXAround function
 * Diffent enum from TileType because we want to be able to look for flags as well.
 */
private object AroundType extends Enumeration {
  type AroundType = Value
  val Bomb, Flag = Value
}

object Tile {
  /**
   * This array contains all the tiles from the current game. Everything is based around this.
   * Private var but you can get it in other namespaces with Tile.getArray
   */
  private var array: Array[Array[Tile]] = Array.ofDim(0)
  private var bombCount: Int = -1

  /**
   * Returns the Tile array that contains all the tiles from the current game
   */
  def getArray: Array[Array[Tile]] = array

  /**
   * Private function used with getBombsAround and getFlagsAround as the base code for them is the same we just need to check for different types.
   * @param x horizontal array index
   * @param y vertical array index
   * @param aroundtype uses "AroundType" enum to know what to check for
   * @return
   */
  private def getXAround(x: Int, y: Int, aroundtype: AroundType): Int = {
    var count: Int = 0

    // check num of X left
    // left
    if (x > 0) {
      if (array(x-1)(y).is(aroundtype)) count += 1
      if (y > 0) if (array(x-1)(y-1).is(aroundtype)) count += 1
      if (y < Window.carrey - 1) if (array(x-1)(y+1).is(aroundtype)) count += 1
    }

    // up/down
    if (y > 0) if (array(x)(y-1).is(aroundtype)) count += 1
    if (y < Window.carrey - 1) if (array(x)(y+1).is(aroundtype)) count += 1

    // right
    if (x < Window.carrex - 1) {
      if (array(x+1)(y).is(aroundtype)) count += 1
      if (y > 0) if (array(x+1)(y-1).is(aroundtype)) count += 1
      if (y < Window.carrey - 1) if (array(x+1)(y+1).is(aroundtype)) count += 1
    }

    count
  }

  /**
   * Get all bombs around specified tile. Used to write number on empty tiles.
   * @param x tile horizontal index
   * @param y tile vertical index
   * @return number of bombs
   */
  def getBombsAround(x: Int, y: Int): Int = {
    if (Tile.getArray(x)(y).isBomb()) return -1
    getXAround(x,y,AroundType.Bomb)
  }
  def getFlagsAround(x: Int, y: Int): Int = getXAround(x,y,AroundType.Flag)

  /**
   * Used at the start of a game to generate the tile array
   * You need to set "Window.carrex" and "Window.carrey" before executing this function
   * @param bombs
   */
  def startupTiles(bombs: Int): Unit = {
    this.bombCount = if (bombs > Window.carrex * Window.carrey) Window.carrex * Window.carrey else bombs
    val bombPos: Array[Int] = Array.fill(bombs)(-1) // Array[Int] of size bombs filled with -1
    val numOfTiles: Int = Window.carrex * Window.carrey

    // random positions for bombs
    for(i <- bombPos.indices) {
      var found: Int = Random.nextInt(numOfTiles)
      while(bombPos.contains(found)) {
        found = Random.nextInt(numOfTiles)
      }
      bombPos(i) = found
    }

    // Array creation
    array = Array.ofDim(Window.carrex, Window.carrey)
    for(x <- array.indices) {
      for (y <- array(x).indices) {
        val isBomb: Boolean = bombPos.contains(Window.carrex * y + x)
        array(x)(y) = new Tile(x,y,if (isBomb) TileType.Bomb else TileType.Empty)
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

    for(i <- Tile.getArray.indices) {
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

  /**
   * Checks if all bombs are flagged (no remaining flags) and all other tiles are not hidden.
   * @return Boolean : game won or not
   */
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
 * @param typeOfTile Tile type found with *TileType* object
 */
class Tile(val x: Int, val y: Int, typeOfTile: TileType) {
  private var hidden: Boolean = true  // All tiles are hidden by default
  private val tileType: TileType = typeOfTile // Tile type is given on construction
  private var flagged: Boolean = false   // On startup, there are no flagged tiles

  def isHidden(): Boolean = hidden
  def isBomb(): Boolean = tileType == TileType.Bomb
  def isFlagged(): Boolean = flagged
  /**
   * If tile is flagged, remove it.
   * If tile isn't flagged, flag it
   * @return new flagged state
   */
  def toggleFlag(): Boolean = {
    flagged = !flagged
    if (flagged) Window.drawFlag(x, y)
    else Window.removeFlag(x, y)
    flagged
  }
  /**
   * set "hidden" to false, so the user can see the interior
   * @return pressed bomb ? true=yes, false=no
   */
  def show(): Boolean = {
    hidden = false
    Window.unHideSquare(x,y)
    isBomb()
  }
  def is(at: AroundType): Boolean = {
    if (at == AroundType.Bomb && tileType == TileType.Bomb) true
    else if (at == AroundType.Flag && flagged) true
    else false
  }
  /**
   * Standard action of clicking a tile
   * @return Game still going, on a "false" return : game is over
   */
  def leftclick(manually: Boolean = false): Boolean = {
    // if click flagged tile, nothing happens
    if (flagged) return true

    if (!hidden) {
      if (manually) return checkFlagsBombsAround()
      return true
    }
    // from now on, we are certain the tile isn't flagged
    // on click of unflagged bomb, end game
    this.show()

    if (typeOfTile == TileType.Bomb) return false
    else if (typeOfTile == TileType.Empty) {
      if (Tile.getBombsAround(x,y) == 0 && !checkFlagsBombsAround()) return false
    }

    true
  }

  /**
   * If you click an empty tile that says "1" and you have one flag around, the program clicks every tile around it. -> classic minesweeper feature
   * @return
   */
  private def checkFlagsBombsAround(): Boolean = {
    val bombsAround: Int = Tile.getBombsAround(x,y)
    val flagsAround: Int = Tile.getFlagsAround(x,y)

    if (bombsAround == flagsAround) if (!leftclickEveryTileAround()) return false

    true
  }
  private def leftclickEveryTileAround(): Boolean = {
    var keepPlaying: Boolean = true

    if (x > 0) {
      if (!Tile.getArray(x-1)(y).leftclick()) keepPlaying = false
      if (y > 0) if (!Tile.getArray(x-1)(y-1).leftclick()) keepPlaying = false
      if (y < Window.carrey - 1) if (!Tile.getArray(x-1)(y+1).leftclick()) keepPlaying = false
    }

    // up/down
    if (y > 0) if (!Tile.getArray(x)(y-1).leftclick()) keepPlaying = false
    if (y < Window.carrey - 1) if (!Tile.getArray(x)(y+1).leftclick()) keepPlaying = false

    // right
    if (x < Window.carrex - 1) {
      if (!Tile.getArray(x+1)(y).leftclick()) keepPlaying = false
      if (y > 0) if (!Tile.getArray(x+1)(y-1).leftclick()) keepPlaying = false
      if (y < Window.carrey - 1) if (!Tile.getArray(x+1)(y+1).leftclick()) keepPlaying = false
    }

    keepPlaying
  }

  /**
   * If isn't flagged : checks if there is still at least 1 flag left. In that case, flags the current tile, else does nothing.
   * If is flagged : removes it
   */
  def rightclick(): Unit = {
    if (!hidden) return
    if (!flagged && Tile.getRemainingFlags() == 0) return
    this.toggleFlag()
  }
}
