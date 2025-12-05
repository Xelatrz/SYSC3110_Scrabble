/**
 * Test for the Game class
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 12/05/2025
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GameModelTest {

    @Test
    @DisplayName("Testing adding a new player")
    void addPlayer() {
        GameModel game = new GameModel();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        game.addPlayer(p1);
        game.addPlayer(p2);

        org.junit.jupiter.api.Assertions.assertEquals(game.players.size(), 2);
    }

    @Test
    @DisplayName("Testing premium square locations")
    void testPremiumSquareLocations() {
        GameModel game = new GameModel();
        int rowTW = 0;
        int colTW= 14;
        int rowDW = 6;
        int colDW = 6;
        int rowTL = 1;
        int colTL = 5;
        int rowDL = 0;
        int colDL = 3;

        org.junit.jupiter.api.Assertions.assertEquals(4, game.getPremiumType(rowTW, colTW));
        org.junit.jupiter.api.Assertions.assertEquals(3, game.getPremiumType(rowDW, colDW));
        org.junit.jupiter.api.Assertions.assertEquals(2, game.getPremiumType(rowTL, colTL));
        org.junit.jupiter.api.Assertions.assertEquals(1, game.getPremiumType(rowDL, colDL));
    }

    @Test
    @DisplayName("Testing undo/redo functionality")
    void testUndoRedo() {
        GameModel game = new GameModel();
        //add players to perform undo/redo
        Player p1 = new Player("p1");
        game.addPlayer(p1);
        game.currentPlayerIndex = 0;

        //add example tiles
        Tile tileT = new Tile("T");
        Tile tileE = new Tile("E");
        Tile tileS = new Tile("S");
        Tile tileT2 = new Tile("T");
        Tile tileX = new Tile("X");
        p1.addTile(tileT);
        p1.addTile(tileE);
        p1.addTile(tileS);
        p1.addTile(tileT2);
        p1.addTile(tileX);

        //must manually add the PlacedTiles to undo stack because that function is in GameController, and GameController requires GUI to test.
        //(I will use duplicated logic to test functionality)
        ArrayList<PlacedTile> undoStack = new ArrayList<>();
        ArrayList<PlacedTile> redoStack = new ArrayList<>();
        int selectedRow;
        int selectedCol;
        Tile tile;

        selectedRow = 6;
        selectedCol = 7;
        tile = tileT;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        selectedRow = 7;
        tile = tileE;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        selectedRow = 8;
        tile = tileS;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        selectedRow = 9;
        tile = tileX;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        selectedRow = 11;
        selectedCol = 8;
        tile = tileT2;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        //test undo
        game.revertAction(undoStack, redoStack);
        game.revertAction(undoStack, redoStack);
        game.revertAction(undoStack, redoStack);
        org.junit.jupiter.api.Assertions.assertEquals("T", game.board.getTile(6, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("E", game.board.getTile(7, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(8, 7));
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(9, 7));
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(11, 8));

        //test redo
        game.revertAction(redoStack, undoStack);
        org.junit.jupiter.api.Assertions.assertEquals("T", game.board.getTile(6, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("E", game.board.getTile(7, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("S", game.board.getTile(8, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(9, 7));
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(11, 8));

        //test clear redo when adding new tiles
        selectedRow = 9;
        selectedCol = 7;
        tile = tileT2;
        game.board.placeTempTile(selectedRow, selectedCol, tile);
        undoStack.add(new PlacedTile(selectedRow, selectedCol, tile));
        redoStack.clear();

        game.revertAction(redoStack, undoStack);
        org.junit.jupiter.api.Assertions.assertEquals("T", game.board.getTile(6, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("E", game.board.getTile(7, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("S", game.board.getTile(8, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertEquals("T", game.board.getTile(9, 7).getLetter());
        org.junit.jupiter.api.Assertions.assertNull(game.board.getTile(11, 8));
    }
}