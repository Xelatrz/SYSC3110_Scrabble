import java.util.*;

public class Tile {
    String letter;
    static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Constructor for Tile class.
     */
    public Tile() {
    }

    public Tile(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public String getRandomLetter() {
        Random  rand = new Random();
        int index = rand.nextInt(alphabet.length());
        char letter = alphabet.charAt(index);
        return String.valueOf(alphabet.charAt(index));
    }
    public void setLetter() {
        letter = getRandomLetter();
    }
}
