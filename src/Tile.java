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
    private String assignedLetter;


    /**
     * Constructs a new Tile with a specified letter.
     * @param letter A string dictating the letter which the tile will have.
     */
    public Tile(String letter) {
        this.letter = letter;
    }

    /**
     * Constructs a new Tile with a specified letter and a specified score.
     * @param letter A string dictating the letter which the tile will have
     * @param score An integer dictating the letter which the tile will have
     */
    public Tile(String letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     * Returns the letter of any tile.
     * @return A string of letter of a specified tile.
     */
    public String getLetter() {
        if (isBlank) {
            return assignedLetter;
        }
        return letter;
    }

    /**
     * Returns the score of the tile
     * @return An integer which is the score of a specified tile
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns if the tile is a blank tile ("-")
     * @return true if the tile is blank, false otherwise
     */
    public boolean isBlank() {
        if (this.letter.equals("-")) {
            isBlank = true;
        }
        return isBlank;
    }

    /**
     * Sets the letter of a tile to be a specified value. Will set a secondary parameter for blank tiles, to avoid
     * overwriting the blank tile entirely for scoring purposes.
     * @param letter A string containing the letter which you want to replace
     */
    public void setLetter(String letter) {
        if (isBlank) {
            assignedLetter = letter;
        }
        this.letter = letter;
    }
}
