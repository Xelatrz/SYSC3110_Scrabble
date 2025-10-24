import java.util.*;
import java.io.*;

public class Game {
    private ArrayList<Player> players;
    private Dictionary acceptedWords;

    /**
     * Constructor for Game class.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
    }

    public static void main(String[] args) {
        Game game = new Game();
    }
}
