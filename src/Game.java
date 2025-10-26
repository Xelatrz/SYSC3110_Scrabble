import java.util.*;

public class Game {
    private static ArrayList<Player> players;
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
        //doesn't add existing player and doesn't add too many players
        if (players.size() < 5 && !players.contains(player)) {
            players.add(player);
        }
    }

    public static void startGame(Board board) {
        Scanner input = new Scanner(System.in);
        boolean turnComplete = false;
        boolean unanimousVote = false;
        boolean gameOver = false;
        Player winner = null;

        //game loop.
        while (!gameOver) {
            for (Player player : players) {
                System.out.println("It is player " + player.getName() + "'s turn.");
                while (!turnComplete) { //loops if turn failed to complete an action.
                    player.showHand();
                    System.out.println("Would you like to PLAY, PASS, or VOTE to end game?");
                    String choice = input.nextLine().toLowerCase(); //both toLowerCase() and equalsIgnoreCase() are used, should we only do one of these two?
                    if (choice.equalsIgnoreCase("play")) {
                        if (player.playWord(board)) {
                            turnComplete = true;
                        }
                    } else if (choice.equalsIgnoreCase("pass")) {
                        if (player.passTurn()) {
                            turnComplete = true;
                        }
                    } else if (choice.equalsIgnoreCase("vote")) {
                        if (player.voteGameOver()) {
                            int numVotes = 1;
                            turnComplete = true;
                            for (int i = 0; i < players.size(); i++) {
                                if (players.get(i) != player) {
                                    System.out.println("It is player " + players.get(i).getName() + "'s turn to vote.");
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
                player.fillHand();

                //game end conditions.
                if (player.emptyHand()) { //AND IF BAG IS EMPTY (add this condition)
                    gameOver = true;
                } else if (unanimousVote) {
                    gameOver = true;
                }

                //game end.
                if (gameOver) {
                    winner = players.getFirst();
                    for (int i = 1; i < players.size(); i++) {
                        if (players.get(i-1).getScore() < players.get(i).getScore()) {
                            winner = players.get(i);
                        }
                    }
                    System.out.println("Game over. The winner is player " + winner.getName());
                }

            }
        }
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

        startGame(board);

        /*
        p1.showHand();
        p1.drawTile();

        p1.showHand();
        p1.playWord(board);
        */
    }
}
