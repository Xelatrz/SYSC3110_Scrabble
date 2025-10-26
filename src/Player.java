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
        System.out.print("Player " + name + "'s hand: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();
    }

    public boolean drawTile() { //update UML return type
        //can only have 7 tiles in scrabble
        if (hand.size() < HAND_SIZE) {
            //draw a tile
            Tile tile = new Tile();
            tile.setLetter();
            hand.add(tile);
            System.out.println("Player " + name + " drew: " + tile.getLetter());
            return true;
        } else {
            System.out.println("Your hand is full");
            return false;
        }
    }

    public void fillHand() {
        while (hand.size() < HAND_SIZE) {
            drawTile();
        }
    }

    public boolean passTurn() {
        //skip the player's turn
        Scanner input = new Scanner(System.in);
        System.out.println(name + "are you sure you'd like to skip your turn? ");
        String choice = input.nextLine().toLowerCase();
        if (choice.equalsIgnoreCase("y")) {
            return true;
        } else if (choice.equalsIgnoreCase("yes")) {
            return true;
        } else if (choice.equalsIgnoreCase("no")) {
            return false;
        } else if (choice.equalsIgnoreCase("n")) {
            return false;
        }
        System.out.println("Invalid choice");
        return false;
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

        System.out.println("Select a row for the tile: ");
        int row = Integer.parseInt(input.nextLine());
        System.out.println("Select a column for the tile: ");
        int col = Integer.parseInt(input.nextLine());

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

        System.out.println("Player " + name + " placed an " + selectedTile.getLetter() + " at " + row + "," + col + "!");
        board.display();
    }

    private void removePlacedTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            Board.removeTile(placedTile.row, placedTile.col, placedTile.tile);
        }
    }

    private void returnPlacedTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            hand.add(placedTile.tile);
        }
    }

    public boolean playWord(Board board) { //needs to be boolean so the player doesn't miss their turn if they placed something incorrectly
        Scanner input = new Scanner(System.in);
        boolean keepGoing = true;
        String choice;
        String placedLetters = "";
        boolean success = true;

        placedTiles.clear(); //make sure placedTiles is empty.

        while (keepGoing) { //let the user place as many tiles as they desire, validity will be determined after.
            showHand();
            placeTile(board);
            System.out.println("Would you like to place another tile? (Y/N)");
            choice = input.nextLine().toLowerCase();
            if (choice.equals("n") || choice.equals("no")) {
                keepGoing = false;
            }
        }
        //check if tiles in placedTiles makes a valid word.
        //words can be left to right or top to bottom.
        //words must be attached to another word.
        //connected words must still be valid.

        if (placedTiles.get(0).row == placedTiles.get(1).row) {
            for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                if (placedTiles.get(i).col != placedTiles.get(i - 1).col) {
                    System.out.println("You cannot place tiles diagonally");
                    success = false;
                }
            }
            placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.row)); //place tiles in order from left to right.
        } else {
            for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                if (placedTiles.get(i).row != placedTiles.get(i - 1).row) {
                    System.out.println("You cannot place tiles diagonally");
                    success = false;
                }
            }
            placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.col)); //place tiles in order from top to bottom.
        }

        for (PlacedTile placedTile : placedTiles) { //create word from placed tiles.
            placedLetters += placedTile.tile.getLetter();
        }

        if (!Game.acceptedWords.checkWord(placedLetters)) { //check if word is an accepted word.
            System.out.println("Your word is invalid!");
            success = false;
        }

        //TO DO LIST:

        //there may be pieces that add to the word (example, the 3rd tile was placed by someone else in a previous turn) check all tiles in the direction the word is going until a tile is null. This may change the current logic on how to check for a valid word.
        //check to make sure the word is touching another word
        //check to make sure surrounding words are still valid
        //for first play of the game, add something to make sure it is valid despite not touching another word (example logic: a word is valid if it either touches a word or touches starting space)
        //add Javadocs where they haven't already been added
        //add a "bag" list of tiles since there is a set number of tiles per game
        //in Player.fillHand() make sure not to draw tiles if bag is empty
        //make sure all input scanners have a tolerance for invalid inputs

        if (success) {
            score += placedTiles.size(); //add score
            //in the future, this will need to account for premium tiles. at that point we may want to make this an actual function.
        } else {
            removePlacedTiles(placedTiles); //remove placed tiles from board.
            returnPlacedTiles(placedTiles); //return placed tiles to hand.
        }
        return success;
    }

    public boolean voteGameOver() {
        Scanner input = new Scanner(System.in);
        System.out.println("Do you want to vote to end game (Y/N)");
        String choice = input.nextLine().toLowerCase();
        if (choice.equals("y") || choice.equals("yes")) {

            return true;
        } else {
            return false;
        }
    }
}

