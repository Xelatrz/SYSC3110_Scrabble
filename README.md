# Scrabble GameModel - Java Implementation with User Interface

A complete Scrabble gameModel implementation in Java that supports up to 2 players, word validation, and most of the classic Scrabble rules.

## Project Files
 - `Board.java` # GameModel board management
 - `Dictionary.java` # Word validation system
 - `GameModel.java` # Main gameModel controller
 - `GameView.java` # Main gameView interface
 - `GameController.java` # Controlls all button logic for the user interface
 - `GameFrame.java` # Creates the game window and JFrame
 - `Player.java` # Player logic and actions
 - `Tile.java` # Tile representation
 - `TileBag.java` # Tile distribution and drawing
 - `PlacedTile.java` # Track placed tiles
 - `scrabble_acceptedwords.csv` # Dictionary file

## Features

- **Multiplayer Support**: 2-4 players
- **Word Validation**: Uses comprehensive dictionary from CSV file --> Currently csv validation is not working, code tested using manual additions to accepted word list. 
- **Classic Rules**: 
  - 15×15 gameModel board
  - Standard tile distribution
  - Valid word placement checks --> Mostly functional but fails in a few specific cases (to be updated later)
  - Scoring system
- **GameModel Flow**:
  - Turn-based gameplay
  - Tile drawing and hand management
  - Pass and end gameModel voting options
  - Win condition detection

## How to Run

### Prerequisites
- Java JDK 21 or higher
- `scrabble_acceptedwords.csv` file in project directory

## GameModel Rules Implementation
### Setup
- Players are initialized with 7 tiles each
- First word must be 2+ letters and placed horizontally/vertically and touch the starting space
- Subsequent words must connect to existing tiles

### Turn Options
- PLAY: Place tiles to form valid words
- PASS: Skip your turn and replace your hand
- VOTE: Propose ending the gameModel (requires unanimous vote)

### Word Validation
- Words must exist in the dictionary

- Tiles must be placed in straight lines (no diagonals)

- Words must connect to existing tiles (after first turn)

- On first turn, tiles must touch the starting space

### End Conditions
- A player uses all tiles AND the tile bag is empty

- A player closes the window using the X button

### Class Responsibilities
**GameModel.java**
- Main gameModel loop and flow control
- Player management
- End gameModel conditions and scoring

**Board.java**
- 15×15 grid management

- Tile placement/removal

- Board state validation

**Player.java**
- Hand management (7 tile limit)

- Tile placement logic

- Word formation and validation

- Scoring calculation

**Dictionary.java**
- Loads words from CSV file

- Validates word existence

- Case-insensitive checking

**TileBag.java**
- Standard Scrabble tile distribution

- Random tile drawing

- Empty state detection

**Tile.java** & **PlacedTile.java**
- Tile representation and tracking

- Coordinate management for placed tiles

## File Format
**Dictionary CSV**
The scrabble_acceptedwords.csv should contain one word per line:
```
apple
banana
computer
```
## Future Enhancements
- The code includes TODO markers for:

- Premium Squares: Double/triple letter/word scores

- Advanced Scoring: Proper point values per letter

- Word Intersection Validation: Check all affected words

- Blank Tiles: Wildcard tile implementation, also check for empty spaces in a word which isn't allowed

- Fixing some of the errors with tile placement validation

Gameplay Example
```
It is player Player 1's turn.
Player 1's hand: A, B, C, D, E, F, G,
Would you like to PLAY, PASS, or VOTE to end gameModel?
> play
Select a tile: A
Select a row for the tile: 7
Select a column for the tile: 7
Player 1 placed an A at 7,7!
```
## Notes
- Currently uses simple scoring (1 point per tile)

- Dictionary file path is hardcoded in GameModel.java

- Maximum 5 players supported


## AI Player Strategy
- AIPlayer checks all possible plays
- AIPlayer makes sure they are capable of placing tiles in the necessary locations
- AIPlayer accepts only legal plays
- Out of the resulting legal plays, AIPlayer chooses the play that will result in the highest score
- If there are no legal moves, AI passes turn


## Contributors
- Daniel Esenwa  101199099 Milestone 1: (Readme) Milestone 2:
- Cole Galway 101302762 Milestone 1: (Main code & unit tests) Milestone 2: (Main GUI code, fixed unit tests, Readme)
- Matthew Gibeault 101323772 Milestone 1: (UML and Sequence Diagrams) Milestone 2: 
- Taylor Brumwell 101302386 Milestone 1: (Main code) Milestone 2: (UML and Sequence Diagrams)
