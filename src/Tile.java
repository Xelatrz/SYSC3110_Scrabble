/**
 * Models a tile from scrabble, generating a random letter from a specified alphabet of letters
 *
 * @author Cole Galway
 * @version 10/26/2025
 */

import java.util.*;

public class Tile {
    /**
     * The letter which will be assigned to the specific tile.
     */
    String letter;
    /**
     * The accepted characters to be on the tiles, in this case the standard english alphabet
     */
    static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Constructs a new Tile, taking no parameters.
     */
    public Tile() {
    }

    /**
     * Constructs a new Tile with a specified letter.
     * @param letter A string dictating the letter which the tile will have.
     */
    public Tile(String letter) {
        this.letter = letter;
    }

    /**
     * Returns the letter of any tile.
     * @return A string of letter of a specified tile
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Returns a random letter from the list of accepted letters.
     * @return A String which contains a random letter.
     */

    public String getRandomLetter() {
        Random  rand = new Random();
        int index = rand.nextInt(alphabet.length());
        char letter = alphabet.charAt(index);
        return String.valueOf(alphabet.charAt(index));
    }

    /**
     * When called this method will assign a random letter to a tile.
     */
    public void setLetter() {
        letter = getRandomLetter();
    }
}
