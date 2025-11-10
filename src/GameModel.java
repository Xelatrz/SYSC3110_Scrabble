import java.util.*;

/**
 * Models the game of Scrabble.
 * A few modifications are neccessary to fully encompass the entire game of scrabble, including a
 * method to add/remove players and have player's input their own names, a method to exchange all
 * the tiles in the hand for another set of tiles in the bag, and a proper scoring system which
 * accounts for tile weights.
 *
 * @author Taylor Brumwell
 * @author Cole Galway
 * @version 11/10/2025
 */
public class GameModel {
    /**
     * The list of players who will be participating in the game.
     */
    public ArrayList<Player> players;
    /**
     * The dictionary of accepted words for the game.
     */
    public static Dictionary acceptedWords;

    public TileBag bag;
    public Board board;

    /** An integer containing the index of the current player */
    private int currentPlayerIndex = 0;

    private List<GameView> views = new ArrayList<>();
    private List<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructs a new Game.
     */
    public GameModel() {
        players = new ArrayList<>();
        acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
        bag = new TileBag();
        board = new Board();
    }

    /**
     * Adds a player into the game, ensuring that the amount of players does not exceed
     * the maximum of five.
     * @param player A Player who will be added into the game.
     */
    public void addPlayer(Player player) {
        //doesn't add existing player and doesn't add too many players
        if (players.size() < 5 && !players.contains(player)) {
            players.add(player);
        }
    }

    /**
     * Sets up a new game with all the neccesary components, and resets the grid, player
     * index and other relevant values to defaults.
     */
    public void setupGame() {
        board = new Board();
        bag  = new TileBag();
        currentPlayerIndex = 0;

        for (Player player : players) {
            player.fillHand(bag);
        }
        placedTiles.clear();
        notifyViews();
    }

    /**
     * Return the current player.
     * @return Player who is currently playing; whose turn it is.
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {return null;}
        return players.get(currentPlayerIndex);
    }

    /**
     * Advances to the next player in the game, skipping the current player.
     */
    public void nextPlayer() {
        if (players.isEmpty()) {return;}
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        notifyViews();
    }

    //very simple scoring logic, needs to be fully implemented later!
    private void scorePlacedTiles(Player p) {
        p.setScore(placedTiles.size());
    }

    //logic for swapping tiles --> needs to be fixed because of a few bugs but almost completed.
    /*
    public void swapTiles(Player p, int tileIndex){
        if (bag.tiles.isEmpty()) {
            return f
        }
        if (p == null || tilesToSwap == null || tilesToSwap.isEmpty()) {return;}
        Collections.sort(tilesToSwap, Collections.reverseOrder());
        int removed = 0;
        for (int index : tilesToSwap) {
            if (index >= 0 && index < p.hand.size()) {
                Tile t = p.removeTileByIndex(index);
                if (t != null) {
                    bag.addTiles(t.getLetter(), 1);
                    removed++;
                }
            }
        }
        for (int i = 0; i < removed; i++) {
            Tile t = bag.drawTile();
            if (t == null) {break;}
            p.addTile(t);
        }
    }

     */

    /**
     * Adds the view to the game model to be able to obverse changes.
     * @param v The GameView used for the GUI.
     */
    public void addView(GameView v) {
        views.add(v);
    }

    /**
     * Notifies the view of a change in the model/game state
     */
    private void notifyViews() {
        for (GameView view : views) {
            view.update(this);
        }
    }
}
