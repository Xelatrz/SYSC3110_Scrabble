import java.util.*;
import java.io.*;

public class Game {
    private ArrayList<Player> players;
    public static Dictionary acceptedWords; //CHANGED TO PUBLIC (update UML)

    /**
     * Constructor for Game class.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public static void main(String[] args) {
        Game game = new Game();
        Board board = new Board();

        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");
        Player p3 = new Player("Player 3");
        Player p4 = new Player("Player 4");
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        p1.fillHand();
        p2.fillHand();
        p3.fillHand();
        p4.fillHand();

        p1.showHand();
        p1.drawTile();

        p1.showHand();
        p1.playWord(board);
    }
}
