import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameFrame extends JFrame implements GameView {
    private JButton[][] grid;
    private JButton[] colMarkers;
    private JButton[] rowMarkers;
    private Board gameBoard;
    private JButton play;
    private JButton swap;
    private JButton pass;
    private JPanel tilePanel;
    private JLabel bagLabel;
    GameModel model;

    private JPanel playerInfo;
    private ArrayList<JLabel> playerScore;
    private ArrayList<JPanel> playerHand;

    private JPanel bottom;

    public GameFrame() {
        super ("Scrabble");
        gameBoard = new Board();
        model = new GameModel();
        model.bag = new TileBag();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int numPlayers = askPlayerCount();
        askPlayerName(model, numPlayers);

        JPanel panel =  new JPanel();
        panel.setSize(800, 600);
        panel.setLayout(new GridLayout(gameBoard.SIZE + 1, gameBoard.SIZE + 1));
        GameController controller = new GameController();

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
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

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

    private void askPlayerName(GameModel model, int count) {
        for (int i = 1; i <= count; i++) {
            String name = JOptionPane.showInputDialog(this, "Enter the player name " + i + ":" ,"Player Name", JOptionPane.QUESTION_MESSAGE);
            if (name.equals(null) ||  name.isEmpty()) {
                name = "Player" + i;
            }
            model.addPlayer(new Player(name));
        }

    }

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

    private void setUpPlayerOptions() {
        bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tilePanel.setPreferredSize(new Dimension(400,60));

        /*
        for (Player p: model.players) {
            JLabel label = new JLabel(p.getName() + "'s tiles:");
            tilePanel.add(label);
        }

         */
        bottom.add(tilePanel, BorderLayout.CENTER);

        //play, swap, pass buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton play = new JButton("Play");
        JButton swap = new JButton("Swap");
        JButton pass = new JButton("Pass");
        play.setBackground(Color.GREEN);
        swap.setBackground(Color.YELLOW);
        pass.setBackground(Color.RED);

        buttonPanel.add(play);
        buttonPanel.add(swap);
        buttonPanel.add(pass);

        bottom.add(buttonPanel, BorderLayout.WEST);
    }

    public void updateRemainingTiles() {
        bagLabel.setText("Tiles remaining" + model.bag.size());
    }
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
    }

}

