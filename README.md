# GOMOKU

Gomoku is an abstract board game that is played on a **19x19** board. The players take turns to place one pawn on the
board with the goal of placing 5 uninterrupted pawns in a line that can be vertical, horizontal or diagonal. There are
many variations of the standard rules that can be decided before starting the game.

## Rules

- ### Three and Three
  players cannot place a pawn if it allows the creation of 2 (or more) lines of three pawns

- ### Four and Four
  players cannot place a pawn if it allows the creation of 2 (or more) lines of four pawns

- ### Handicap
  alternatively, if a player breaks the _three and three_ rule for the first time, the opponent must place **2 pawns**
  on his turn

## Opening moves

- ### No restrictions
- ### Pro
    1. Player 1 must place his first pawn in the center of the board,
    2. Player 2 can place his pawn anywhere in the board
    3. Player 1 now must place his second pawn three intersections away from the center
    4. the game continues normally from here
- #### many others...

## Variations

- ### Standard
  the player must line up **only** 5 pawns. Lines of 6 or more are called _overlines_ and are not permitted
- ### Freestyle
  **overlines** can win the game
- ### Renju
  changes the size of the board to a **15x15**
- ### Omok
  set of rules that include a standard board, Three and Three rule, and allows overlines
- #### many others...

_[Sources](https://en.wikipedia.org/wiki/Gomoku)_

* AI player uses code from [DLV2](https://github.com/veltri/DLV2) by [Università della Calabria](https://www.mat.unical.it/calimeri/projects/embasp/)