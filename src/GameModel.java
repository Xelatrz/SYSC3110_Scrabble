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
    int currentPlayerIndex = 0;

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

    /*
    public boolean placeTile(Player p, Tile tile, int row, int col) {
        notifyViews();
        if (p == null || tile == null || board == null) {
            return false;
        }
        if (row < 0 || col < 0 || row >= Board.SIZE || col >= Board.SIZE) {
            return false;
        }
        if (board.getTile(row, col) != null) {
            return false;
        }

        board.placeTile(row, col, tile);
        placedTiles.add(new PlacedTile(row, col, tile));

        notifyViews();
        return true;
    }

     */

    //would be redundant if scorePlacedTiles was public, but that should be private so others can't change their score, only the game can.
    //WILL BE CALLED (only not called yet because of an error)
    public int simulateScore(ArrayList<PlacedTile> placedTiles) {
        return scorePlacedTiles(placedTiles, null);
    }


    private int scorePlacedTiles(ArrayList<PlacedTile> placedTiles, Player player) {
        boolean sameRow = placedTiles.stream().allMatch(pt -> pt.row == placedTiles.getFirst().row);
        boolean sameCol = placedTiles.stream().allMatch(pt-> pt.col == placedTiles.getFirst().col);

        //invalid move (should never be called, but to be caught in case
        if (!sameRow && !sameCol) {
            return 0;
        }

        int totalScore = 0;

        StringBuilder word = new StringBuilder();
        int wordScore = 0;

        if (sameRow) {
            int row = placedTiles.getFirst().row;
            int col = placedTiles.getFirst().col;

            int start = col;
            while (start > 0 && board.getTile(row, start - 1) != null ) {
                start -= 1;
            }

            int end = col;
            while (end < board.SIZE - 1 && board.getTile(row, end + 1) != null) {
                end += 1;
            }
            for (int c = start; c <= end; c++) {
                Tile t = board.getTile(row, c);
                if (t == null) {
                    return 0; //shouldn't happen but there for safety
                }
                word.append(t.getLetter());
                wordScore += t.getScore();
            }
        } else {
            int col = placedTiles.getFirst().col;
            int row =  placedTiles.getFirst().row;

            int start = row;
            while (start > 0 && board.getTile(start - 1, col) != null) {
                start -= 1;
            }
            int end = row;
            while (end < board.SIZE - 1 && board.getTile(end + 1, col) != null) {
                end += 1;
            }

            for (int r = start; r <= end; r++) {
                Tile t = board.getTile(r, col);
                if (t == null) {
                    return 0;
                }
                word.append(t.getLetter());
                wordScore += t.getScore();
            }
        }
        //should only happen in the case of checking the perpendicular words.
        if (!acceptedWords.checkWord(word.toString())) {
            return 0;
        }
        totalScore += wordScore;

        //checking the perpendicular word
        for (PlacedTile pt: placedTiles) {
            StringBuilder cross = new StringBuilder();
            int crossScore = 0;

            if (sameRow) {
                int r =  pt.row;
                int start = r;
                while (start > 0 && board.getTile(start - 1, pt.col)!= null) {
                    start -= 1;
                }
                int end = r;
                while (end < board.SIZE - 1 && board.getTile(end + 1, pt.col)!= null) {
                    end += 1;
                }

                //cross word is only 1 tile long
                if (end - start >= 1) {
                    for (int row = start; row <= end; row++) {
                        Tile t = board.getTile(row, pt.col);
                        cross.append(t.getLetter());
                        crossScore += t.getScore();
                    }
                    if (!acceptedWords.checkWord(cross.toString())) {
                        return 0;
                    }
                    totalScore +=  crossScore;
                }
            } else {
                int c = pt.col;
                int start = c;
                while (start > 0 && board.getTile(pt.row, start - 1) != null) {
                    start -= 1;
                }
                int end = c;
                while(end < board.SIZE - 1 && board.getTile(end + 1, c) != null) {
                    end += 1;
                }
                if (end - start >= 1) {
                    for (int row = start; row <= end; row++) {
                        Tile t = board.getTile(row, c);
                        cross.append(t.getLetter());
                        crossScore += t.getScore();
                    }
                }
                if (!acceptedWords.checkWord(cross.toString())) {
                    return 0;
                }
                totalScore +=  crossScore;
            }
        }
        if (player != null) {
            player.setScore(player.getScore() + totalScore);
        }
        return totalScore;
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
    void notifyViews() {
        for (GameView view : views) {
            view.update(this);
        }
    }
}
