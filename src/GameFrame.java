/**
 * The JFrame for the user interface for the Scrabble Game.
 *
 * @author Cole Galway
 * @version November 10th, 2025
 */

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameFrame extends JFrame implements GameView {
    private JButton[][] grid;
    private JButton[] colMarkers;
    private JButton[] rowMarkers;
    private Board gameBoard;
    private JPanel tilePanel;
    private JLabel bagLabel;
    private GameModel model;
    private GameController controller;

    private JPanel playerInfo;
    private ArrayList<JLabel> playerScore;
    private ArrayList<JPanel> playerHand;

    private JPanel bottom;

    /**
     * Constructs a new GameFrame, taking no parameters.
     */
    public GameFrame() {
        super ("Scrabble");
        model = new GameModel();
        model.acceptedWords.load("scrabble_acceptedwords.csv");
        this.setLayout(new BorderLayout());

        int numPlayers = askPlayerCount();
        askPlayerName(model, numPlayers);

        model.setupGame();
        gameBoard = model.board;


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel =  new JPanel();
        panel.setSize(800, 600);
        panel.setLayout(new GridLayout(gameBoard.SIZE + 1, gameBoard.SIZE + 1));

        controller = new GameController(model, this);
        model.addView(this);

        grid = new JButton[gameBoard.SIZE][gameBoard.SIZE];
        colMarkers = new JButton[gameBoard.SIZE];
        rowMarkers = new JButton[gameBoard.SIZE];

        panel.add(new JLabel(""));

        //set up the frame around the grid marker
        JButton marker;
        for (int col = 0; col < gameBoard.SIZE; col++) {
            marker = new JButton(String.valueOf(col + 1));
            colMarkers[col] = marker;
            marker.setBackground(Color.DARK_GRAY);
            marker.setEnabled(false);
            panel.add(marker);
        }

        for (int row = 0; row < gameBoard.SIZE; row++) {
            marker = new JButton(String.valueOf(row + 1));
            rowMarkers[row] = marker;
            marker.setBackground(Color.DARK_GRAY);
            marker.setEnabled(false);
            panel.add(marker);

            //set up the buttons
            JButton button;
            for (int col = 0; col < gameBoard.SIZE; col++) {
                button = new JButton(" ");
                button.setActionCommand(row + "," + col);
                button.setPreferredSize(new Dimension(40, 40));
                grid[row][col] = button;
                button.addActionListener(controller);
                panel.add(button);

            }
        }
        setUpPlayerInfo();
        this.add(playerInfo, BorderLayout.EAST);
        setUpPlayerOptions();
        this.add(bottom, BorderLayout.SOUTH);
        updateRemainingTiles();
        updateTilePanel(model);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Prints a menu which will ask the player how many players wish to play the game, and stores
     * this numeral data in an integer.
     * @return An int of the number of players
     */
    private int askPlayerCount() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter your number of players (2-4: ", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                System.exit(0);
            }
            try {
                int count = Integer.parseInt(input);
                if (count >= 2 && count <= 4) {
                    return count;
                }
            } catch (NumberFormatException ignored) {
                JOptionPane.showMessageDialog(this, "Invalid input!, Please enter a number between 2 and 4", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Asks the players for their names.
     * @param model The GameModel which contains all game behaviour
     * @param count The number of players in the current game.
     */

    private void askPlayerName(GameModel model, int count) {
        for (int i = 1; i <= count; i++) {
            String name = JOptionPane.showInputDialog(this, "Enter the player name " + i + ":" ,"Player Name", JOptionPane.QUESTION_MESSAGE);
            if (name.equals(null) ||  name.isEmpty()) {
                name = "Player" + i;
            }
            model.addPlayer(new Player(name));
        }

    }

    /**
     * Sets up the player panel on the right side of the game window. Contains information about player name, score
     * and the tiles left in the bag.
     */
    private void setUpPlayerInfo() {
        playerInfo = new JPanel();
        playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.Y_AXIS));
        playerScore = new ArrayList<>();
        playerHand = new ArrayList<>();

        for (Player p: model.players) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel scoreLabel = new JLabel(p.getName()+ "'s Score: " + p.getScore());
            playerScore.add(scoreLabel);

            panel.add(scoreLabel, BorderLayout.NORTH);

            playerInfo.add(panel);
        }

        bagLabel = new JLabel("Tiles remaining: " + model.bag.size());
        bagLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        bagLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerInfo.add(bagLabel);

    }

    /**
     * Sets up the player options panel on the bottom of the game window. Contains the buttons for play, swap and pass
     * and the tiles in each player's hand.
     */
    private void setUpPlayerOptions() {
        bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tilePanel.setPreferredSize(new Dimension(400,60));
        bottom.add(tilePanel, BorderLayout.CENTER);

        //play, swap, pass buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton play = new JButton("Play");
        JButton swap = new JButton("Swap");
        JButton pass = new JButton("Pass");
        play.setBackground(Color.GREEN);
        swap.setBackground(Color.YELLOW);
        pass.setBackground(Color.RED);

        play.setActionCommand("Play");
        swap.setActionCommand("Swap");
        pass.setActionCommand("Pass");

        play.addActionListener(controller);
        swap.addActionListener(controller);
        pass.addActionListener(controller);

        buttonPanel.add(play);
        buttonPanel.add(swap);
        buttonPanel.add(pass);

        bottom.add(buttonPanel, BorderLayout.WEST);
    }

    /**
     * Updates the number of tiles remaining in the TileBag.
     */
    public void updateRemainingTiles() {
        bagLabel.setText("Tiles remaining" + model.bag.size());
    }

    /**
     * Updates the player's hand after a turn has been exectued.
     * @param model The GameModel where all the game logic is stored.
     */
    public void updateTilePanel(GameModel model) {
        tilePanel.removeAll();

        Player current = model.getCurrentPlayer();

        JLabel playerLabel = new  JLabel(current.getName() + "'s tiles: ", SwingConstants.CENTER);
        tilePanel.add(playerLabel);

        tilePanel.add(Box.createVerticalStrut(10));

        ArrayList<Tile> hand = current.getHand();

        for (int i = 0; i < hand.size(); i++) {
            Tile t = hand.get(i);

            JButton button = new JButton(t.getLetter());
            button.setActionCommand("TILE:" + i);
            button.addActionListener(controller);

            tilePanel.add(button);

        }
        tilePanel.revalidate();
        tilePanel.repaint();
    }

    /**
     * Updates the player's score displayed in the player panel.
     */
    public void updatePlayerScore() {
        for (int i = 0; i < model.players.size(); i++) {
            Player p = model.players.get(i);
            playerScore.get(i).setText(p.getName() + "'s Score: " + p.getScore());
        }
    }

    /**
     * Displays the error messages when a player incorrectly chooses an option.
     * @param message the error message that is to be displayed
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates the game board to display tile changes.
     * @param model A GameModel that contains all of the game logic
     */
    public void updateBoard(GameModel model) {
        for (int row = 0; row < model.board.SIZE; row++) {
            for (int col = 0; col < model.board.SIZE; col++) {
                Tile t = model.board.getTile(row, col);
                grid[row][col].setText(t == null ? " " : t.getLetter());
            }
        }
    }

    /**
     * Updates the game window from the view.
     * @param model A GameModel containing all the game logic.
     */
    public void update(GameModel model) {
        updateBoard(model);
        updatePlayerScore();
        updateTilePanel(model);
        updateRemainingTiles();
        revalidate();
        repaint();

    }


    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        Dictionary dict = new Dictionary();
        dict.addWord("able");
        dict.addWord("cat");
        dict.addWord("dog");
        dict.addWord("table");
        dict.addWord("a");
        dict.addWord("b");
        dict.addWord("c");
        dict.addWord("d");
        dict.addWord("e");
        dict.addWord("f");
        dict.addWord("g");
        dict.addWord("h");
        System.out.println(dict.acceptedWords.contains("cat"));
    }

}

