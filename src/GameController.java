/**
 * The controller for the Scrabble game, handles all UI cases and user inputs
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 11/24/2025
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameController implements ActionListener {
    private GameModel model;
    private GameView view;

    /** The tile selected by the user */
    private Integer selectedTileIndex = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    /** An array of tiles that have been placed by a player in their turn */
    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    /** stack of undo operations */
    private ArrayList<PlacedTile> undoStack = new ArrayList<>();
    /** stack of redo operations */
    private ArrayList<PlacedTile> redoStack = new ArrayList<>();

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
            case "Undo":
                handleUndo();
                break;
            case "Redo":
                handleRedo();
                break;
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
     * Handles an undo button click
     */
    private void handleUndo() {
        model.revertAction(undoStack, redoStack);
        view.update(model);
    }

    /**
     * Handles a redo button click
     */
    private void handleRedo() {
        model.revertAction(redoStack, undoStack);
        view.update(model);
    }

    /**
     * Advances to the next player in the game, skipping the current player.
     */
    public void nextPlayer() {
        if (model.players.isEmpty()) {
            return;
        }
        undoStack.clear();
        redoStack.clear();
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
        if (selectedTileIndex == null || selectedRow < 0 || selectedCol < 0) {
            view.showError("Select a tile and a board space!");
            return;
        }
        Player p = model.getCurrentPlayer();
        Tile tile = p.getHand().get(selectedTileIndex);

        boolean ok = model.board.placeTempTile(selectedRow, selectedCol, tile);
        if (!ok) {
            view.showError("Select an unoccupied space!");
            return;
        }

        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

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

        for (PlacedTile pt: placedTiles) {
            if (pt.tile.isBlank()) {
                char chosen = view.promptBlankLetter();
                pt.tile.setLetter(String.valueOf(chosen).toUpperCase());
            }
        }

        boolean valid = p.playWord(model.board, placedTiles);

        if (valid) {
            model.board.commitTiles(placedTiles);
            p.fillHand(model.bag);
            int score = model.scorePlacedTiles(placedTiles);
            p.setScore(p.getScore() + score);
            view.update(model);
            nextPlayer();
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
     * The logic that executes once the "Swap" button has been pressed.
     */
    private void handleSwap() {
        Player p = model.getCurrentPlayer();
        ArrayList<Tile> tilesToSwap = new ArrayList<>();
        if (selectedTileIndex == null) {
            view.showError("Select a tile first!");
            return;
        }
        Tile t = p.removeTileByIndex(selectedTileIndex);
        if (t != null) {
            model.bag.addTile(t);
            p.fillHand(model.bag);
            selectedTileIndex = null;
            view.update(model);
        }
    }

    /**
     * Handles the logic for what occurs after the "Pass" button is pressed.
     */
    private void handlePass() {
        Player p = model.getCurrentPlayer();
        for (Tile t: p.hand) {
            model.bag.addTile(t);
        }
        p.hand.clear();
        p.fillHand(model.bag);
        view.update(model);
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

    /**
     * The logic for handling the turns of AI players in the scrabble game.
     */
    private void handleAITurn() {
        AIPlayer ai = (AIPlayer) model.getCurrentPlayer();
        Move bestMove = ai.findBestMove();

        if (bestMove == null) {
            handlePass();
            return;
        }

        placedTiles.clear();
        for (PlacedTile pt : bestMove.placedTiles) {
            if (model.board.getPermTile(pt.row, pt.col) == null) {
                placedTiles.add(pt);
            }
        }
        if (placedTiles.isEmpty()) {
            handlePass();
            return;
        }

        for (PlacedTile pt : placedTiles) {
            model.board.placeTempTile(pt.row, pt.col, pt.tile);
        }
        model.board.commitTiles(placedTiles);
        model.board.clearTempGrid();

        ArrayList<String> removeLetters = new ArrayList<>();
        for (PlacedTile pt : placedTiles) {
            removeLetters.add(pt.tile.getLetter().toUpperCase());
        }
        for (int letter = 0; letter < removeLetters.size(); letter++) {
            String letterToRemove = removeLetters.get(letter);
            for (int i = 0; i < ai.getHand().size(); i++) {
                if (ai.getHand().get(i).getLetter().equalsIgnoreCase(letterToRemove)) {
                    ai.removeTileByIndex(i);
                    break;
                }
            }
        }

        ai.fillHand(model.bag);

        int turnScore = model.scorePlacedTiles(placedTiles);
        ai.setScore(ai.getScore() + turnScore);

        placedTiles.clear();
        clearSelections();
        view.update(model);

        nextPlayer();
    }
}