/**
 * Models a tile from scrabble.
 *
 * @author Cole Galway
 * @version 10/26/2025
 */
public class Tile {
    /**
     * The letter which will be assigned to the specific tile.
     */
    String letter;


    /**
     * Constructs a new Tile with a specified letter.
     * @param letter A string dictating the letter which the tile will have.
     */
    public Tile(String letter) {
        this.letter = letter;
    }

    /**
     * Returns the letter of any tile.
     * @return A string of letter of a specified tile.
     */
    public String getLetter() {
        return letter;
    }
}
