import java.util.*;
import java.io.Serializable;

/**
 * This class models the bag of tiles in a standard scrabble game.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 12/05/2025
 */
public class TileBag implements Serializable {
    /**
     * A list of characters with each of the tiles and the number of them inside the bag.
     */
    public final List <String> TILES = new ArrayList<>();
    /**
     * A random variable from the Random class.
     */
    private final Random RAND = new Random();

    /**
     * Constructs a new TileBag, taking each letter from the English alphabet and assigning the number of
     * times it appears in a standard scrabble game.
     */
    public TileBag() {
        addTiles("E", 12);
        addTiles("A", 9);
        addTiles("I", 9);
        addTiles("O", 8);
        addTiles("N", 6);
        addTiles("R", 6);
        addTiles("T", 6);
        addTiles("L", 4);
        addTiles("S", 4);
        addTiles("U", 4);
        addTiles("D", 4);
        addTiles("G", 3);
        addTiles("B", 2);
        addTiles("C", 2);
        addTiles("M", 2);
        addTiles("P", 2);
        addTiles("F", 2);
        addTiles("H", 2);
        addTiles("V", 2);
        addTiles("W", 2);
        addTiles("Y",2);
        addTiles("K", 1);
        addTiles("J", 1);
        addTiles("Q", 1);
        addTiles("Z", 1);
        addTiles("-", 2);
    }

    /**
     * Adds a tile into the TileBag, including the number of times that tile will be inside the bag.
     * @param letter A character which represents the letter on the tile.
     * @param count An integer representing the amount of each specific character is inside the bag.
     */
    public void addTiles(String letter, int count) {
        for (int i = 0; i < count; i++) {
            TILES.add(letter);
        }
    }

    /**
     * Returns the Tile which has been drawn from the bag.
     * @return Tile which has been selected at random from the bag
     */
    public Tile drawTile() {
        if (TILES.isEmpty()) {
            return null; //no tiles are left in the bag
        }
        String letter = TILES.remove(RAND.nextInt(TILES.size()));
        int score = scoreLetter(letter);
        return new Tile(String.valueOf(letter), score);
    }

    /**
     * Returns true or false depending on if the bag of tiles has been emptied.
     * @return true if the bag is empty, false if the bag is not empty.
     */
    public boolean isEmpty() {
        return TILES.isEmpty();
    }

    public int  size() {
        return TILES.size();
    }

    public void addTile(Tile t) {
        if (t != null && t.getLetter() != null) {
            TILES.add(t.getLetter());
        }
    }

    public int scoreLetter(String letter) {
        switch (letter) {
            case "A": case "E": case "I": case "O": case "U": case "L": case "N": case "S": case "T": case "R":
                return 1;
            case "D": case "G":
                return 2;
            case "B": case "C": case "M": case "P":
                return 3;
            case "F": case "H": case "V": case "W": case "Y":
                return 4;
            case "K":
                return 5;
            case "J": case "X":
                return 8;
            case "Q": case "Z":
                return 10;
            default:
                return 0;
        }
    }
}
