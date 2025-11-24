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

public class GameController implements ActionListener {
    private GameModel model;
    private GameView view;

    /**
     * The tile selected by the user
     */
    private Integer selectedTileIndex = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    /**
     * An array of tiles that have been placed by a player in their turn
     */
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    /**
     * Constructs a new GameController, taking the Model and View as parameters
     *
     * @param model A GameModel that contains all of the game logic
     * @param view  A GameView interface that updates the model
     */
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Overrides the existing actionPerformed method to handle the game specifications
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(null)) {
            return;
        }

        if (command.startsWith("TILE:")) {
            String num = command.substring(5);
            try {
                int index = Integer.parseInt(num);
                selectedTileIndex = index;
            } catch (NumberFormatException ex) {
            }
            return;
        }

        if (command.contains(",")) {
            String[] coord = command.split(",");
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
     * Advances to the next player in the game, skipping the current player.
     */
    public void nextPlayer() {
        if (model.players.isEmpty()) {
            return;
        }
        model.board.clearTempGrid();
        placedTiles.clear();
        clearSelections();

        model.currentPlayerIndex = (model.currentPlayerIndex + 1) % model.players.size();
        model.notifyViews();

        if (model.getCurrentPlayer() instanceof AIPlayer) {
            handleAITurn();
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
        if (placedTiles.isEmpty()) {
            view.showError("Please place a tile");
            return;
        }
        Player p = model.getCurrentPlayer();
        boolean valid = p.playWord(model.board, placedTiles);

        if (valid) {
            model.board.commitTiles(placedTiles);
            p.fillHand(model.bag);
            int score = model.simulateScore(placedTiles);
            p.setScore(p.getScore() + score);
            view.update(model);
            nextPlayer(); //ERROR: when next player is an AI player, the important stuff at the end of this function is never reached. fix this for other turn options as well.
        } else {
            for (PlacedTile pt : placedTiles) {
                p.addTile(pt.tile);
            }
            model.board.clearTempGrid();
            placedTiles.clear();
            clearSelections();
            view.showError("Invalid word. Please try again.");
            view.update(model);
        }
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
        //Isn't this supposed to replace hand?
        nextPlayer();
    }

    /**
     * Clears the user selections from the grid and hand.
     */
    private void clearSelections() {
        selectedTileIndex = null;
        selectedRow = -1;
        selectedCol = -1;
    }

    private void handleAITurn() {
        AIPlayer ai = (AIPlayer) model.getCurrentPlayer();
        Move bestMove = ai.findBestMove(model.board);

        if (bestMove == null) {
            handlePass();
            return;
        }

        boolean willPlace = false;
        for (PlacedTile pt : bestMove.placedTiles) {
            if (model.board.getTile(pt.row, pt.col) == null) {
                willPlace = true;
                break;
            }
        }

        if (!willPlace) {
            handlePass();
            return;
        }

        for (PlacedTile pt : bestMove.placedTiles) {
            model.board.placeTempTile(pt.row, pt.col, pt.tile);
        }
        model.board.commitTiles(bestMove.placedTiles);

        ai.fillHand(model.bag);

        ai.setScore(ai.getScore() + bestMove.score);

        view.update(model);
        nextPlayer();
    }
}