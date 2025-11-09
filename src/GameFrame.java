import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame implements GameView {
    private JButton[][] grid;
    private JButton[] colMarkers;
    private JButton[] rowMarkers;
    private Board gameBoard;
    GameModel model;

    public GameFrame() {
        super ("Scrabble");
        gameBoard = new Board();
        model = new GameModel();
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
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
    }

}

