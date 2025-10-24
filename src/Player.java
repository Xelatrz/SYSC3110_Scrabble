import java.util.*;

public class Player {
    String name;
    int score;
    ArrayList<Tile> hand = new ArrayList<>();
    static int HAND_SIZE = 7;

    /**
     * Constructor for Player class.
     * @param name The name of the player being constructed
     */
    public Player(String name) {
        this.name = name;
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

    //this method might be better somewhere else? in the game menu
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

    public void placeTile(Board board) {
        Scanner input =  new Scanner(System.in);
        showHand(); //we may not want this here, but for now i'm leaving it.

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

        if (row >= board.SIZE || col >= board.SIZE) {
            System.out.print("That spot is out of bounds.");
            return;
        }

        board.placeTile(row, col, selectedTile);
        hand.remove(selectedTile);

        System.out.println("Player " + name + " placed an " +  selectedTile.getLetter() + " at " + row + "," + col + "!");
        board.display();

    }


    public static void main(String[] args) {
        Player p1 = new Player("Player 1");
        Board board = new Board();
        p1.fillHand();
        p1.showHand();

        p1.drawTile();

        p1.showHand();

        p1.placeTile(board);
    }

}

