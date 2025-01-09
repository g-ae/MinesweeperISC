# MinesweeperISC
Project for the 101.1 class at the HES-SO Valais Wallis by Lucas Henry and Gonçalo Esteves

## How to play
- Launch the game from IntelliJ, the main menu will be displayed. \
![Game launched](img/mainmenu.png)
- Choose the difficulty you want by left-clicking it. The game will then start. \
![Game started](img/gamestarted.png)
- You can left-click on the tiles to check for bombs and right-click to place a flag.
![Checked tile and flagged](img/checkandflag.png)
- Have fun !

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

## FunGraphics
Version 1.5.15 \
[Download from GitHub](https://github.com/ISC-HEI/FunGraphics/releases/tag/1.5.15)