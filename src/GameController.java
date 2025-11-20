/**
 * The controller for the Scrabble game, handles all UI cases and user inputs
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 11/10/2025
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameController implements ActionListener {
    private GameModel model;
    private GameView view;

    /** The tile selected by the user */
    private Integer selectedTileIndex = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    /**An array of tiles that have been placed by a player in their turn*/
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructs a new GameController, taking the Model and View as parameters
     * @param model A GameModel that contains all of the game logic
     * @param view A GameView interface that updates the model
     */
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Overrides the existing actionPerformed method to handle the game specifications
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command =  e.getActionCommand();
        if (command.equals(null)) {return;}

        if (command.startsWith("TILE:")) {
            String num = command.substring(5);
            try {
                int index =  Integer.parseInt(num);
                selectedTileIndex = index;
            } catch (NumberFormatException ex) {}
            return;
        }

        if (command.contains(",")) {
            String [] coord = command.split(",");
            selectedRow = Integer.parseInt(coord[0]);
            selectedCol = Integer.parseInt(coord[1]);
            placeTileTemporarily();
            return;
        }
        switch (command) {
            case "Play":
                handlePlay();
                break;
            case "Swap":
                handleSwap();
                break;
            case "Pass":
                handlePass();
            default:
        }
    }

    /**
     * Places a tile on the temporary grid until confirmation and validation by the
     * play button.
     */
    private void placeTileTemporarily() {
        Player p = model.getCurrentPlayer();
        if (selectedTileIndex == null || selectedRow < 0 || selectedCol < 0) {
            view.showError("Select a tile and a board space!");
            return;
        }
        Tile tile = p.getHand().get(selectedTileIndex);
        model.board.placeTempTile(selectedRow, selectedCol, tile);
        placedTiles.add(new PlacedTile(selectedRow, selectedCol, tile));
        p.removeTileByIndex(selectedTileIndex);

        clearSelections();
        view.update(model);

    }

    /**
     * The actions which occurs after the "play" button is pressed.
     */
    private void handlePlay() {
        if (placedTiles.isEmpty()){
            view.showError("Please place a tile");
            return;
        }
        Player p = model.getCurrentPlayer();
        boolean valid = p.playWord(model.board, placedTiles);

        if (valid) {
            model.board.commitTiles(placedTiles);
            p.fillHand(model.bag);
            model.nextPlayer();
        } else {
            for (PlacedTile pt: placedTiles) {
                p.addTile(pt.tile);
            }
            model.board.clearTempGrid();
            view.showError("Invalid word. Please try again.");
        }
        placedTiles.clear();
        clearSelections();
        view.update(model);
    }

    /**
     * Handles the "Swap" button logic, needs to be properly implemented.
     */
    private void handleSwap() {
        Player p = model.getCurrentPlayer();
        if (selectedTileIndex == null) {
            view.showError("Select a tile first!");
            return;
        }
        /*
        boolean ok = model.swapTiles(p, );
        */
    }

    /**
     * Handles the logic for what occurs after the "Pass" button is pressed.
     */
    private void handlePass() {
        placedTiles.clear();
        model.nextPlayer();
        clearSelections();
    }

    /**
     * Clears the user selections from the grid and hand.
     */
    private void clearSelections() {
        selectedTileIndex = null;
        selectedRow = -1;
        selectedCol = -1;
    }

    //MUST IMPLEMENT SOMETHING TO TELL WHEN TO PLAY AI VS HUMAN TURN
    private void handleAITurn(AIPlayer ai) {
        Move bestMove = ai.findBestMove(model.board);

        if (bestMove == null) {
            handlePass();
            model.nextPlayer(); //check if we call this here. Also, handle whether that player is AI or human
            return;
        }

        for (PlacedTile pt: placedTiles) {
            model.board.placeTempTile(pt.row, pt.col, pt.tile);
        }

        //WILL LIKELY NEED TO ADJUST THIS SCORING SYSTEM
        //find score for this play
        //add score

        model.board.commitTiles(placedTiles);
        ai.fillHand(model.bag);
        model.nextPlayer();
    }

    /*
    //first AI attempt (not currently used, but keeping it for now in case some logic proves useful later).
    private void handleAITurn(AIPlayer ai) {
        if (model.board.isEmpty()) {
            Tile bestTile = ai.hand.stream().max(Comparator.ComparingInt(Tile::getValue)).get();
            model.board.placeTempTile(model.board.CENTRE, model.board.CENTRE, bestTile);
            placedTiles.add(new PlacedTile(model.board.CENTRE, model.board.CENTRE, bestTile));
            model.board.commitTiles(placedTiles); //treat this as an anchor, but DON'T COMMIT YET****
        }

        ArrayList<Point> anchors = findAnchors(model.board);
        List<List<Tile>> permutations = getAllPermutations(ai.hand);

        int bestScore = -1;
        ArrayList<Tile> bestPlay = null;

        for (Point p: anchors) {
            int row = anchor.x;
            int col = anchor.y;

            for (List<Tile> permutation: permutations) {
                //horizontal
                ArrayList<PlacedTile> horizontal = tryPlacement(row, col, permutation, true, ai);
                if (horizontal != null) {
                    int score = scoreHorizontal(row, col, model.board);
                    if (score > bestScore) {
                        bestScore = score;
                        bestPlay = horizontal;
                    }
                }
                model.board.clearTempGrid();

                //vertical
                ArrayList<PlacedTile> vertical = tryPlacement(row, col, permutation, false, ai);
                if (vertical != null) {
                    int score = scoreVertical(row, col, model.board);
                    if (score > bestScore) {
                        bestScore = score;
                        bestPlay = vertical;
                    }
                }
                model.board.clearTempGrid();
            }
        }

        if (bestPlay == null) {
            handlePass();
            return;
        }
        model.board.commitTiles(bestPlay); //must first play the word, then verify. Input takes PlacedTile, not Tile.
    }
    */
}
