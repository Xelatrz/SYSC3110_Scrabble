import java.util.*;


public class Player {
    private String name;
    private int score;
    private static int HAND_SIZE = 7;
    private ArrayList<Tile> hand;
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructor for Player class.
     *
     * @param name The name of the player being constructed
     */
    public Player(String name) {
        this.name = name;
        score = 0;
        hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean emptyHand() {
        return hand.isEmpty();
    }

    public void showHand() {
        System.out.print(name + "'s hand: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();
    }

    public boolean drawTile(TileBag bag) { //update UML return type
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

    public void fillHand(TileBag bag) {
        while (hand.size() < HAND_SIZE && !bag.isEmpty()) {
            drawTile(bag);
        }
    }

    public void placeTile(Board board) { //update UML parameters
        Scanner input = new Scanner(System.in);

        //takes the user input for the tile
        System.out.println("Select a tile: ");
        String selectedLetter = input.nextLine().toLowerCase();

        //finds the tile in the hand
        Tile selectedTile = null;
        for (Tile tile : hand) {
            if (tile.getLetter().equalsIgnoreCase(selectedLetter)) {
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
                row = Integer.parseInt(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, row number must be an integer.");
            }
        }
        int col;
        while (true) {
            try {
                System.out.println("Select a column for the tile: ");
                col = Integer.parseInt(input.nextLine());
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

    private void removePlacedTiles(Board board, ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            board.removeTile(placedTile.row, placedTile.col, placedTile.tile);
        }
    }

    private void returnPlacedTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            hand.add(placedTile.tile);
        }
    }

    public boolean playWord(Board board) {
        Scanner input = new Scanner(System.in);
        boolean keepGoing = true;
        String choice;
        boolean success = true;

        placedTiles.clear(); //make sure placedTiles is empty.

        //let the user place as many tiles as they desire, validity will be determined after.
        while (keepGoing) {
            showHand();
            placeTile(board);

            while (true) {
                System.out.println("Would you like to place another tile? (Y/N)");
                choice = input.nextLine().toLowerCase();
                if (choice.equals("n") || choice.equals("no")) { //assume word is complete if player no longer wants to place more tiles.
                    keepGoing = false;
                    break;
                } else if (choice.equals("y") || choice.equals("yes")) {
                    break;
                }
                System.out.println("Invalid input, please answer yes or no. (Y/N)");

            }
        }

        //check if tiles in placedTiles makes a valid word.
        //words can be left to right or top to bottom.
        //words must be attached to another word.
        //connected words must still be valid.
        int firstCoord = 0;
        int lastCoord = 0;
        int wordLength = 0;
        String placedLetters = "";
        if (placedTiles.size() == 1) { //doesn't check for diagonals on single tiles
            success = true;
        } else {
            if (placedTiles.get(0).row == placedTiles.get(1).row) {
                for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                    if (placedTiles.get(i).col != placedTiles.get(i - 1).col) {
                        System.out.println("You cannot place tiles diagonally");
                        success = false;
                    }
                }
                placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.row)); //place tiles in order from left to right.

                //check for tiles before/after placed tiles
                int col = placedTiles.getFirst().col;
                for (int i = 0; placedTiles.getFirst().row - i >= 0 && board.getTile(placedTiles.getFirst().row - i, col) != null; i++) {
                    firstCoord = placedTiles.getFirst().row - i;
                }
                for (int i = 0; placedTiles.getLast().row + i <= 15 && board.getTile(placedTiles.getLast().row + i, col) != null; i++) {
                    lastCoord = placedTiles.getLast().row + i;
                }

                //create word from placed tiles.
                wordLength = lastCoord - firstCoord + 1;
                for (int i = firstCoord; i < wordLength + firstCoord; i++) {
                    placedLetters += board.getTile(i, col).getLetter();
                }
            } else {
                for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                    if (placedTiles.get(i).row != placedTiles.get(i - 1).row) {
                        System.out.println("You cannot place tiles diagonally");
                        success = false;
                    }
                }
                placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.col)); //place tiles in order from top to bottom.

                //check for tiles before/after placed tiles
                int row = placedTiles.getFirst().row;
                for (int i = 0; board.getTile(row, placedTiles.getFirst().col - i) != null; i++) {
                    firstCoord = placedTiles.getFirst().col - i;
                }
                for (int i = 0; board.getTile(row, placedTiles.getLast().col + i) != null; i++) {
                    lastCoord = placedTiles.getLast().col + i;
                }

                //create word from placed tiles.
                wordLength = lastCoord - firstCoord + 1;
                for (int i = firstCoord; i < wordLength + firstCoord; i++) {
                    placedLetters += board.getTile(row, i).getLetter();
                }
            }

            //check if word is an accepted word.
            if (!Game.acceptedWords.checkWord(placedLetters)) {
                System.out.println("Your word is not an accepted word!");
                success = false;
            }

            //check to make sure the word is touching another word.
            boolean boardEmpty = true;
            for (int r = 0; r < Board.SIZE; r++) {
                for (int c = 0; c < Board.SIZE; c++) {
                    if (board.getTile(r, c) != null ) {
                        boardEmpty = false;
                        break;
                    }
                }
                if (!boardEmpty) {
                    break;
                }
            }
            if (!boardEmpty && wordLength <= placedTiles.size()) {
                System.out.println("Your word must be connected to another word");
                success = false;
            }


            //TO DO LIST:

            //if the player places only one tile to complete an almost existing word, the system will fail to determine the direction the word is going.
            //check to make sure surrounding words are still valid
            //for first play of the game, add something to make sure it is valid despite not touching another word (example logic: a word is valid if it either touches a word or touches starting space)
            //add Javadocs where they haven't already been added

            if (success) {
                score += placedTiles.size(); //add score
                //in the future, this will need to account for premium tiles. at that point we may want to make this an actual function.
            } else {
                removePlacedTiles(board, placedTiles); //remove placed tiles from board.
                returnPlacedTiles(placedTiles); //return placed tiles to hand.
            }
        }
            return success;

    }

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

