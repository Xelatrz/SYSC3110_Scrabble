/**
 * The JFrame for the user interface for the Scrabble Game.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 12/05/2025
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
        model.acceptedWords.load("scrabble_acceptedwords.csv"); //are we able to remove this (happens in model already)
        this.setLayout(new BorderLayout());

        String[] options = {"New Game", "Load Game"};
        int option = JOptionPane.showOptionDialog(this,"Start a new game or load a saved game?", "Start Game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (option == 1) {
            GameModel loaded = GameModel.loadGame("saved_game.ser");
            if (loaded != null) {
                model = loaded;
                gameBoard = model.board;
                JOptionPane.showMessageDialog(this,"Game has been loaded!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load game, starting new game", "Load Game", JOptionPane.ERROR_MESSAGE);
                setupNewGame();
            }
        } else {
            setupNewGame();
        }

        gameBoard = model.board;
        if (model.views == null) {
            model.views = new ArrayList<>();
        }
        model.addView(this);

        controller = new GameController(model, this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveOption = new JMenuItem("Save Game");

        fileMenu.add(saveOption);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        saveOption.addActionListener(e-> {model.saveGame("saved_game.ser");
        JOptionPane.showMessageDialog(this, "Game saved successfully!", "Save Game", JOptionPane.INFORMATION_MESSAGE);});

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

                button.setBackground(getPremium(row, col));
            }
        }
        updateBoard(model);
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

    private void askPlayerInfo(GameModel model, int count) {
        for (int i = 1; i <= count; i++) {
            boolean human = false;
            String name;

            if (i == 1) {
                name = JOptionPane.showInputDialog(this, "Player 1 must be human. Enter the player name " + i + ":" ,"Player Name", JOptionPane.QUESTION_MESSAGE);
                if (name.equals(null) ||  name.isEmpty()) {
                    name = "Player" + i;
                }
                model.addPlayer(new Player(name));
            } else {
                String input = JOptionPane.showInputDialog(this, "Will player " + i + " be human or AI", "Human or AI", JOptionPane.QUESTION_MESSAGE);
                input = input.toLowerCase();
                if (input.equals("human")) { //else AI (defaults to AI since it's possible to play with other AI players, but not with extra humans that don't exist)
                    human = true;
                }
                if (human) {
                    name = JOptionPane.showInputDialog(this, "Enter the player name " + i + ":" ,"Player Name", JOptionPane.QUESTION_MESSAGE);
                    if (name.equals(null) ||  name.isEmpty()) {
                        name = "Player" + i;
                    }
                    model.addPlayer(new Player(name));
                } else {
                    name = "AIPlayer" + i;
                    model.addPlayer(new AIPlayer(name, model));
                }
            }
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
        JButton undo = new JButton("Undo");
        JButton redo = new JButton("Redo");
        play.setBackground(Color.GREEN);
        swap.setBackground(Color.YELLOW);
        pass.setBackground(Color.RED);
        undo.setBackground(Color.PINK);
        redo.setBackground(Color.PINK);

        play.setActionCommand("Play");
        swap.setActionCommand("Swap");
        pass.setActionCommand("Pass");
        undo.setActionCommand("Undo");
        redo.setActionCommand("Redo");

        play.addActionListener(controller);
        swap.addActionListener(controller);
        pass.addActionListener(controller);
        undo.addActionListener(controller);
        redo.addActionListener(controller);

        buttonPanel.add(play);
        buttonPanel.add(swap);
        buttonPanel.add(pass);
        buttonPanel.add(undo);
        buttonPanel.add(redo);

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

    /**
     * Returns a char that the player will assign to a blank tile, prompts the user to type in
     * a value for the blank tile. defaults to A if no letter is typed.
     * @return A char which contains the new letter for the blank tile.
     */
    public char promptBlankLetter() {
        String input;
        do {
            input = JOptionPane.showInputDialog(this, "Please enter a letter for the blank tile:  ");
            if (input == null) {
                return 'A';
            }
            input = input.trim().toUpperCase();
        } while (input.length() != 1 || !Character.isLetter(input.charAt(0)));
        return  input.charAt(0);
    }

    /**
     * Return the background colour for any tile.
     * @param row The row of the tile
     * @param col The column of the tile
     * @return The Colour which the background of the tile will be.
     */
    private Color getPremium(int row, int col) {
        Board.Premium p = gameBoard.getPremium(row, col);
        return switch(p) {
            case TRIPLE_WORD -> Color.RED;
            case DOUBLE_WORD, CENTER -> Color.PINK;
            case TRIPLE_LETTER -> Color.BLUE;
            case DOUBLE_LETTER -> Color.CYAN;
            default -> Color.WHITE;
        };
    }

    /**
     * Sets up a new game of scrabble, prompting the user to choose number of players
     * and other details
     */
    private void setupNewGame() {
        model = new GameModel();
        int numPlayers = askPlayerCount();
        askPlayerInfo(model, numPlayers);

        String[] choices = {"Standard", "Custom 1", "Custom 2"};
        String choice = (String) JOptionPane.showInputDialog(this, "Select a game board:", "Board Selection", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

        model.setupGame();
        gameBoard = model.board;

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                gameBoard.setPremium(r, c, Board.Premium.NORMAL);
            }
        }
        if ("Standard".equals(choice)) {
            gameBoard.setDefaultBoard();
        } else if ("Custom 1".equals(choice)) {
            BoardLoader.importBoardXML(gameBoard, "custom_board1.xml");
        } else {
            BoardLoader.importBoardXML(gameBoard, "custom_board2.xml");
        }

    }


    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
    }

}

