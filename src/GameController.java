import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameController implements ActionListener {
    private GameModel model;
    private GameView view;

    private Integer selectedTileIndex = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private ArrayList<PlacedTile> placedTiles = new ArrayList<>();

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

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

    private void completeTurn() {
        Player p = model.getCurrentPlayer();

        boolean success = p.playWord(model.board, placedTiles);

        if (success) {
            model.board.commitTiles(placedTiles);
            model.nextPlayer();
        } else {
            for (PlacedTile pt: placedTiles) {
                p.addTile(pt.tile);
            }
            model.board.clearTempGrid();
        }
        placedTiles.clear();
        view.update(model);

    }

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

    private void handlePass() {
        placedTiles.clear();
        model.nextPlayer();
        clearSelections();
    }

    private void clearSelections() {
        selectedTileIndex = null;
        selectedRow = -1;
        selectedCol = -1;
    }
}
