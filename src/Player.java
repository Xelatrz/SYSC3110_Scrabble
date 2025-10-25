import java.util.*;


public class Player {
    private String name;
    public int score;
    private static int HAND_SIZE = 7;
    private ArrayList<Tile> hand;
    private ArrayList<PlacedTile> placedTiles =  new ArrayList<>();

    /**
     * Constructor for Player class.
     * @param name The name of the player being constructed
     */
    public Player(String name) {
        this.name = name;
        score = 0;
        hand = new ArrayList<>();
    }

    public void showHand() {
        System.out.print("Player " + name + "'s hand: ");
        for (Tile tile : hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();
    }

    public void drawTile() {
        //can only have 7 tiles in scrabble
        if (hand.size() < HAND_SIZE) {
            //draw a tile
            Tile tile = new Tile();
            tile.setLetter();
            hand.add(tile);
            System.out.println("Player " + name + " drew: " + tile.getLetter());
        } else {
            System.out.println("Your hand is full");
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
        System.out.println(name + "you'd like to skip your turn? ");
        String choice = input.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            return true;
        }
        else if (choice.equalsIgnoreCase("yes")) {
            return true;
        }
        else if (choice.equalsIgnoreCase("no")) {
            return false;
        }
        else if (choice.equalsIgnoreCase("n")) {
            return false;
        }
        System.out.println("Invalid choice");
        return false;
    }

    public void placeTile(Board board) { //update UML parameters
        Scanner input =  new Scanner(System.in);
        showHand(); //we may not want this here, but for now i'm leaving it.

        //takes the user input for the tile
        System.out.println("Select a tile: ");
        String selectedLetter = input.nextLine().toLowerCase();

        //finds the tile in the hand
        Tile selectedTile = null;
        for (Tile tile : hand) {
            if (tile.getLetter().equalsIgnoreCase(selectedLetter)) { //both lower and ignore case are used, should we only do one of these two?
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

        System.out.println("Player " + name + " placed an " +  selectedTile.getLetter() + " at " + row + "," + col + "!");
        board.display();
    }

    public boolean playWord(Board board) { //needs to be boolean so the player doesn't miss their turn if they placed something incorrectly
        Scanner input = new Scanner(System.in);
        boolean keepGoing = true;
        String choice;
        String placedLetters = "";

        placedTiles.clear(); //make sure placedTiles is empty.

        while (keepGoing) { //let the user place as many tiles as they desire, validity will be determined after.
            placeTile(board);
            System.out.println("Would you like to place another tile? (Y/N)");
            choice = input.nextLine().toLowerCase();
            if (choice.equals("n")) {
                keepGoing = false;
            }
        }

        //check if tiles in placedTiles makes a valid word.
        //words can be left to right or top to bottom.
        //words must be attached to another word.
        //connected words must still be valid.

        if (placedTiles.get(0).row == placedTiles.get(1).row) {
            for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                if (placedTiles.get(i).col != placedTiles.get(i-1).col) {
                    System.out.println("You cannot place tiles diagonally");
                    return false;
                }
            }
            placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.row)); //place tiles in order from left to right.
        } else {
            for (int i = 1; i < placedTiles.size(); i++) { //check for diagonals (diagonals not allowed).
                if (placedTiles.get(i).row != placedTiles.get(i-1).row) {
                    System.out.println("You cannot place tiles diagonally");
                    return false;
                }
            }
            placedTiles.sort(Comparator.comparingInt(placedTile -> placedTile.col)); //place tiles in order from top to bottom.
        }

        for (PlacedTile placedTile : placedTiles) { //create word from placed tiles.
            placedLetters += placedTile.tile.getLetter();
        }

        if (!Game.acceptedWords.checkWord(placedLetters)) { //check if word is an accepted word.
            System.out.println("Your word is invalid!");
            for (PlacedTile placedTile : placedTiles) {
                Board.removeTile(placedTile.row, placedTile.col, placedTile.tile);
            }
            return false;
        }

        //check to make sure the word is touching another word (go in more detail on how)*

        //check to make sure surrounding words are still valid (go in more detail on how)*


        //add score
        score += placedTiles.size();
        //in the future, this will need to account for premium tiles. at that point we may want to make this an actual function.

        return true;
    }
}

