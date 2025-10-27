import java.util.*;

/**
 * Models a player from a game of scrabble, storing the player name, score, and their hand of tiles.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 10/27/2025
 */
public class Player {
    /**
     * The player's name, representing as a string.
     */
    private String name;
    /**
     * The player's score, representing as an integer.
     */
    private int score;
    /**
     * Standard scrabble hand only has a max of  7 tiles.
     */
    private static int HAND_SIZE = 7;
    /**
     * A List of tiles which is the hand of the player.
     */
    private ArrayList<Tile> hand;
    /**
     * A list of the tiles that have been placed on the board.
     */
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructs a new Player.
     *
     * @param name The name of the player being constructed
     */
    public Player(String name) {
        this.name = name;
        score = 0;
        hand = new ArrayList<>();
    }

    /**
     * Returns the name of the player.
     * @return A String with the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's score.
     * @return A integer containing the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns if the player's hand is empty.
     * @return True if the hand is empty, false otherwise.
     */
    public boolean emptyHand() {
        return hand.isEmpty();
    }

    /**
     * Display's the hand of the player.
     */
    public void showHand() {
        System.out.print(name + "'s hand: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();
    }

    /**
     * Draws a tile from the bag of tiles and adds it to the player's hand.
     * Returns if the tile was added to the player's hand or not.
     * @param bag A TileBag containing a specific amount of tiles
     * @return True if the tile was added, false if not.
     */
    public boolean drawTile(TileBag bag) {
        //can only have 7 tiles in scrabble
        if (hand.size() < HAND_SIZE) {
            //draw a tile
            Tile tile = bag.drawTile();
            if (tile == null) {
                System.out.println("No tiles left!");
                return false;
            }
            hand.add(tile);
            System.out.println(name + " drew: " + tile.getLetter());
            return true;
        } else {
            System.out.println("Your hand is full");
            return false;
        }
    }

    /**
     * When called this method will fill the player's hand with tiles, until they have a full hand.
     * It will not fill the player's hand if their hand is full, or the bag is empty.
     * @param bag The TileBag which contains the specific tiles and their quantities
     */
    public void fillHand(TileBag bag) {
        while (hand.size() < HAND_SIZE && !bag.isEmpty()) {
            drawTile(bag);
        }
    }

    /**
     * Places a tile onto a specific location on the board based on the player's input.
     * @param board The specific game board which the players are using
     */
    public void placeTile(Board board) {
        Scanner input = new Scanner(System.in);

        //takes the user input for the tile
        System.out.println("Select a tile: ");
        String selectedLetter = input.nextLine().toUpperCase();

        //finds the tile in the hand
        Tile selectedTile = null;
        for (Tile tile : hand) {
            if (tile.getLetter().equals(selectedLetter)) {
                selectedTile = tile;
                break;
            }
        }
        if (selectedTile == null) {
            System.out.println("That tile isn't in " + name + "'s hand!");
            return;
        }

        //place tile on desired board coordinate
        int row;
        while (true) {
            try {
                System.out.println("Select a row for the tile: ");
                row = Integer.parseInt(input.nextLine().toLowerCase());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, row number must be an integer.");
            }
        }
        int col;
        while (true) {
            try {
                System.out.println("Select a column for the tile: ");
                col = Integer.parseInt(input.nextLine().toLowerCase());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, column number must be an integer.");
            }
        }

        if (board.getTile(row, col) != null) {
            System.out.println("That spot is already occupied.");
            return;
        }

        if (row < 0 || col < 0 || row >= board.SIZE || col >= board.SIZE) {
            System.out.print("That spot is out of bounds.");
            return;
        }

        board.placeTile(row, col, selectedTile);
        hand.remove(selectedTile);
        placedTiles.add(new PlacedTile(row, col, selectedTile));

        System.out.println(name + " placed an " + selectedTile.getLetter() + " at " + row + "," + col + "!");
        board.display();
    }

    /**
     * Removes placed tiles from the board.
     * @param board A Board for the game which is being used by the players
     * @param placedTiles A List of tiles that have been placed on the board
     */
    private void removePlacedTiles(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            board.removeTile(placedTile.row, placedTile.col, placedTile.tile);
        }
    }

    /**
     * Gives back the tiles that were placed into the player's hand.
     * @param placedTiles A List of the tiles that have been placed on the board.
     */
    private void returnPlacedTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            hand.add(placedTile.tile);
        }
    }

    /**
     * Returns if a word was played on the board or not, allows a player to place tiles on the board
     * and validates if the tiles are correctly placed and if they form a valid word.
     * @param board A Board which is being used for the game
     * @return true if the word was valid and the play was completed, false otherwise.
     */
    public boolean playWord(Board board) {
        Scanner input = new Scanner(System.in);
        boolean keepGoing = true;
        boolean success = true;
        String choice;
        String word = "";
        boolean firstTurn = board.isEmpty(board); //check before placing tiles

        placedTiles.clear(); //make sure placedTiles is empty.

        //let the user place as many tiles as they desire, validity will be determined after.
        while (keepGoing) {
            showHand();
            placeTile(board);
            while (true) { //make sure input is valid.
                System.out.println("Would you like to place another tile? (Y/N)");
                choice = input.nextLine().toLowerCase();
                if (choice.equals("n") ||  choice.equals("no")) {
                    keepGoing = false;
                    break;
                } else if (choice.equals("y") || choice.equals("yes")) {
                    keepGoing = true;
                    break;
                } else {
                    System.out.println("Invalid input, please answer yes or no. (Y/N)");
                }
            }
        }

        //if only 1 tile is placed, skip diagonal check
        if (placedTiles.size() == 1) {
            success = true;
        } else {
            boolean sameRow = placedTiles.stream().allMatch(t -> t.row == placedTiles.getFirst().row);
            boolean sameCol = placedTiles.stream().allMatch(t -> t.col == placedTiles.getFirst().col);

            if (!sameRow && !sameCol) {
                System.out.println("You cannot place tiles diagonally");
                success = false;
            } else {
                if (sameRow) { //horizontal word
                    int row = placedTiles.getFirst().row;
                    // sort by column
                    placedTiles.sort(Comparator.comparingInt(t -> t.col));

                    //find first and last column
                    int firstCol = placedTiles.getFirst().col;
                    int lastCol = placedTiles.getLast().col;

                    while (firstCol > 0 && board.getTile(row, firstCol - 1) != null) {
                        firstCol--;
                    }
                    while (lastCol < board.SIZE - 1 && board.getTile(row, lastCol + 1) != null) {
                        lastCol++;
                    }

                    //build the word from the tiles
                    for (int c = firstCol; c <= lastCol; c++) {
                        Tile tile = board.getTile(row, c);
                        if (tile != null) {
                            word += tile.getLetter();
                        }
                    }
                } else { //vertical word
                    int col = placedTiles.getFirst().col;
                    // sort by row
                    placedTiles.sort(Comparator.comparingInt(t -> t.row));

                    //find first and last column
                    int firstRow = placedTiles.getFirst().row;
                    int lastRow = placedTiles.getLast().row;

                    while (firstRow > 0 && board.getTile(firstRow - 1 ,col) != null) {
                        firstRow--;
                    }
                    while (lastRow < board.SIZE - 1 && board.getTile(lastRow + 1,col) != null) {
                        lastRow++;
                    }

                    //build the word from the tiles
                    for (int r = firstRow; r <= lastRow; r++) {
                        Tile tile = board.getTile(r, col);
                        if (tile != null) {
                            word += tile.getLetter();
                        }
                    }
                }

                //check dictionary to see if the word is valid
                if (!Game.acceptedWords.checkWord(word)) {
                    System.out.println("Your word is not an accepted word!");
                    success = false;
                }
            }
        }

        // check that words connect (except on the first play)
        if ((!firstTurn) && (word.length() <= placedTiles.size()) && (word.length() > 1)) {
            System.out.println("Your word must be connected to another word");
            success = false;
        } else if (firstTurn) {
            //(TO BE IMPLEMENTED when special board spaces are added).
            //make sure word touches the board's starting space
            if (placedTiles.size() <= 1) { //make sure more than one tile is placed on first turn
                System.out.println("First word of the game must be longer than one tile.");
                success = false;
            }
        }

        //(TO BE IMPLEMENTED).
        //check to make sure the surrounding words are still valid.
        //account for blank spaces in the word.

        //placeholder for scoring
        if (success) {
            int pointsGained = placedTiles.size();
            score += pointsGained; //add score
            System.out.println(name + " scored " + pointsGained + " points. " + name + "'s score is now " + score);
            //in the future, this will need to account for premium tiles. at that point we may want to make this an actual function.
        } else {
            removePlacedTiles(board, placedTiles); //remove placed tiles from board.
            returnPlacedTiles(placedTiles); //return placed tiles to hand.
        }
        return success;
    }

    /**
     * Skips a player's turn upon their request
     * Returns if the turn was skipped.
     * @return true if the turn was skipped, false otherwise.
     */
    public boolean passTurn() {
        //skip the player's turn
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(name + ", are you sure you'd like to skip your turn? (Y/N)");
            String choice = input.nextLine().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
            System.out.println("Invalid input, please answer yes or no. (Y/N)");
        }
    }

    /**
     * Allows player's to vote to end the game.
     * Returns if the game was voted to be ended.
     * @return true if the game is voted to be ended by unanimous vote, false otherwise.
     */
    public boolean voteGameOver() {
        Scanner input = new Scanner(System.in);
        System.out.println(name + ", do you want to vote to end game (Y/N)");
        String choice = input.nextLine().toLowerCase();
        while(true) {
            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
            System.out.println("Invalid input, please answer yes or no. (Y/N)");
        }

    }
}

