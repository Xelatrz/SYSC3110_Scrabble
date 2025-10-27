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
public class Game {
    /**
     * The list of players who will be participating in the game.
     */
    private static ArrayList<Player> players;
    /**
     * The dictionary of accepted words for the game.
     */
    public static Dictionary acceptedWords; //CHANGED TO PUBLIC (update UML)

    /**
     * Constructs a new Game.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.acceptedWords = new Dictionary();
        acceptedWords.load("scrabble_acceptedwords.csv");
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
     * Once called this method will start the game, and handle all the game logic and the general
     * game loop for each player and their turn.
     * @param board A Board which is used for the game.
     */
    public static void startGame(Board board) {
        Scanner input = new Scanner(System.in);
        boolean turnComplete = false;
        boolean unanimousVote = false;
        boolean gameOver = false;
        TileBag bag = new TileBag();

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
                        if (player.passTurn()) {
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
    public static void endGame() {
        Player winner = players.getFirst();
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i-1).getScore() < players.get(i).getScore()) {
                winner = players.get(i);
            }
        }
        System.out.println("Game over. The winner is " + winner.getName());
    }

    public static void main(String[] args) {
        Game game = new Game();
        Board board = new Board();
        TileBag bag = new TileBag();

        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");
        Player p3 = new Player("Player 3");
        Player p4 = new Player("Player 4");
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);

        p1.fillHand(bag);
        p2.fillHand(bag);
        p3.fillHand(bag);
        p4.fillHand(bag);

        startGame(board);
    }
}
