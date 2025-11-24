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
    private String letter;
    private int score;
    private boolean isBlank;


    /**
     * Constructs a new Tile with a specified letter.
     * @param letter A string dictating the letter which the tile will have.
     */
    public Tile(String letter) {
        this.letter = letter;
    }

    public Tile(String letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     * Returns the letter of any tile.
     * @return A string of letter of a specified tile.
     */
    public String getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    public boolean isBlank() {
        return this.isBlank;
    }

    public void setLetter(String letter) {}
}
