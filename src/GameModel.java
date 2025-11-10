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
 * @version 10/27/2025
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

    private int currentPlayerIndex = 0;

    /**
     * Constructs a new Game.
     */
    public GameModel() {
        players = new ArrayList<>();
        acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
        bag = new TileBag();
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

    public void setupGame() {
        board = new Board();
        if (bag == null) {bag = new TileBag();}
        for (Player player : players) {
            player.fillHand(bag);
        }
        currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {return null;}
        return players.get(currentPlayerIndex);
    }

    public void nextPlayer() {
        if (players.isEmpty()) {return;}
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean placeTile(Player p, Tile tile, int row, int col) {
        if (p == null || tile == null || board == null) {return false;}
        if (row < 0 || col < 0 || row >= Board.SIZE || col >= Board.SIZE) {return false;}
        if (board.getTile(row, col) != null) {return false;}
        board.placeTile(row, col, tile);
        return true;
    }
    /**
     * Once called this method will start the game, and handle all the game logic and the general
     * game loop for each player and their turn.
     */
    public void startGame() {
        Board board = new Board();
        bag = new TileBag();

        Scanner input = new Scanner(System.in);
        boolean turnComplete;
        boolean unanimousVote = false;
        boolean gameOver = false;

        //game setup
        if (players.size() < 2|| players.size() > 4) {//make sure there are 2 to 4 players
            System.out.println("2-4 players are required to play Scrabble.");
            return;
        }
        for (Player player : players) {
            player.fillHand(bag);
        }

        //game loop.
        while (!gameOver) {
            for (Player player : players) {
                turnComplete = false;
                System.out.println("It is player " + player.getName() + "'s turn.");

                while (!turnComplete) { //loops if turn failed to complete an action.
                    player.showHand();
                    System.out.println("Would you like to PLAY, PASS, or VOTE to end game?");
                    String choice = input.nextLine().toLowerCase();
                    if (choice.equals("play")) {
                        if (player.playWord(board)) {
                            turnComplete = true;
                        }
                    } else if (choice.equals("pass")) {
                        if (player.passTurn(bag)) {
                            turnComplete = true;
                        }
                    } else if (choice.equals("vote")) {
                        if (player.voteGameOver()) {
                            int numVotes = 1;
                            turnComplete = true;
                            for (int i = 0; i < players.size(); i++) {
                                if (players.get(i) != player) {
                                    System.out.println("It is " + players.get(i).getName() + "'s turn to vote.");
                                    if(players.get(i).voteGameOver()) {
                                        numVotes ++;
                                    }
                                }
                            }
                            if (numVotes == players.size()) {
                                unanimousVote = true;
                            }
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                player.fillHand(bag);

                //game end conditions.
                if (player.emptyHand() && bag.isEmpty()) {
                    gameOver = true;
                } else if (unanimousVote) {
                    gameOver = true;
                }

                //game end.
                if (gameOver) {
                    endGame();
                    return;
                }

            }
        }
    }

    /**
     * Terminates the game upon being called, and determines the winner based off the score.
     * Prints the winner of the game.
     */
    public void endGame() {
        Player winner = players.getFirst();
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i-1).getScore() < players.get(i).getScore()) {
                winner = players.get(i);
            }
        }
        System.out.println("Game over. The winner is " + winner.getName());
    }

    public void main(String[] args) {
        GameModel gameModel = new GameModel();

        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");
        Player p3 = new Player("Player 3");
        Player p4 = new Player("Player 4");
        gameModel.addPlayer(p1);
        gameModel.addPlayer(p2);
        gameModel.addPlayer(p3);
        gameModel.addPlayer(p4);

        startGame();
    }
}
