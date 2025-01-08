# MinesweeperISC
Project for the 101.1 class at the HES-SO Valais Wallis

## General idea
2D Array of "Tile"s (class made by us) \
Tile class would have three variables inside : 
- **Boolean**: hidden (default: true) 
- **String**: Type of tile (Bomb, Empty)
- **Boolean**: flag (is the tile flagged?)
  - *true*: user thinks a bomb is here (unclickable)
  - *false*: no flag

On every user's click, the graphical interface is updated. Checks every element in the array and displays it accordingly. \
When every bomb has been flagged, and every non-bomb tile is !hidden, end game. \
If the user clicks an unflagged bomb, the game ends as well.

## Fungraphic 
Version 1.5.15