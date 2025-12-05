# Scrabble GameModel - Java Implementation with User Interface

A complete Scrabble gameModel implementation in Java that supports up to 2 players, word validation, and most of the classic Scrabble rules.

## Project Files
 - `Board.java` # GameModel board management
 - `BoardLoader.java` #Loads boards from XML files into the game
 - `Dictionary.java` # Word validation system
 - `GameModel.java` # Main gameModel controller
 - `GameView.java` # Main gameView interface
 - `GameController.java` # Controlls all button logic for the user interface
 - `GameFrame.java` # Creates the game window and JFrame
 - `Player.java` # Player logic and actions
 - `AIPlayer.java` #AI Player logic and actions
 - `Tile.java` # Tile representation
 - `TileBag.java` # Tile distribution and drawing
 - `PlacedTile.java` # Track placed tiles
 - `scrabblewords.csv` # Dictionary file

## Features

- **Multiplayer Support**: 2-4 players
- **Supports up to 3 AI Players:** Can handle 3 AI players and 1 human player playing together. 
- **Word Validation**: Uses comprehensive dictionary from CSV file. 
- **Classic Rules**: 
  - 15×15 gameModel board
  - Standard tile distribution
  - Standard Scrabble Premium Tiles
  - Valid word placement checks
  - Scoring system
- **Custom Boards**: Supports 2 custom boards defined in `custom_board1.xml/custom_board2.xml`
- **GameModel Flow**:
  - Turn-based gameplay
  - Tile drawing and hand management
  - Pass and end gameModel voting options
  - Win condition detection
  - Undo/Redo move functionality

## How to Run
- See attached User Manual for running instructions

### Prerequisites
- Java JDK 21 or higher
- `scrabblewords.csv` file in project directory
- For custom game boards, `custom_board1.xml` and `custom_board2.xml` files in project directory

## GameModel Rules Implementation
### Setup
- Players are initialized with 7 tiles each
- First word must touch the starting space
- Subsequent words must connect to existing tiles

### Turn Options
- PLAY: Place tiles to form valid words
- PASS: Skip your turn and replace your hand
- SWAP: Swaps a tile from the player's hand for a new tile, returns the old tile to the bag
- UNDO: Undoes the most recent move from a player
- REDO: Undoes the undo of the most recent move, replacing the tile. 

### Word Validation
- Words must exist in the dictionary

- Tiles must be placed in straight lines (no diagonals)

- Words must connect to existing tiles (after first turn)

- On first turn, tiles must touch the starting space

- Score's the word formed on each turn by the corresponding values of each tile

### End Conditions
- A player uses all tiles AND the tile bag is empty

- A player closes the window using the X button

### Class Responsibilities
**GameModel.java**
- Main gameModel loop and flow control
- Player management
- Handle AIPlayer turn
- End gameModel conditions and scoring
- Premium tile logic and score calculations

**Board.java**
- 15×15 grid management
- Tile placement/removal
- Board state validation

**BoardLoader.java**
- Loads the custom boards from XML files into usable data

**Player.java**
- Hand management (7 tile limit)
- Tile placement logic
- Word formation and validation
- Scoring calculation

**AIPlayer.java**
- AIPlayer strategy
- Validation AI plays

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
The scrabblewords.csv should contain one word per line:
```
apple
banana
computer
```

## Notes
- Maximum 5 players supported
- Minimum 1 human player required


## AI Player Strategy
- AIPlayer checks all possible plays
- AIPlayer makes sure they are capable of placing tiles in the necessary locations
- AIPlayer accepts only legal plays
- Out of the resulting legal plays, AIPlayer chooses the play that will result in the highest score
- If there are no legal moves, AI passes turn


## Contributors
- Daniel Esenwa  101199099 Milestone 1: (Readme) Milestone 2:  Milestone 3: (Sequence Diagrams) Milestone 4:
- Cole Galway 101302762 Milestone 1: (Main code & unit tests) Milestone 2: (Main GUI code, fixed unit tests, Readme) Milestone 3: (Blank tile code implementation, premium tiles code implementation, score code implementation, Unit tests, Readme + JavaDoc comments + Data Structures Explanation document) Milestone 4: (Serilization/deserialization code implementation, Custom board functionality code implementation, ReadMe, User Manual, Sequence Diagrams, Data structures docuemnt, JUnit Testing, JavaDoc + Code comments)
- Matthew Gibeault 101323772 Milestone 1: (UML and Sequence Diagrams) Milestone 2:  Milestone 3: (UML Diagrams) Milestone 4:
- Taylor Brumwell 101302386 Milestone 1: (Main code) Milestone 2: (UML and Sequence Diagrams) Milestone 3: (AI player code implementation, bug fixes, AI player unit tests, AI player strategy README explanation, JavaDoc comments) Milestone 4: (Undo/Redo functionalilty code implementation, JavaDoc comments, UML diagram, Sequence Diagram, Junit testing)
