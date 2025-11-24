/**
 * Test for the TileBag class
 *
 * @author Cole Galway
 * @version 10/27/2025
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TileBagTest {

    @Test
    @DisplayName ("Testing adding tiles")
    void addTiles() {
        TileBag bag = new TileBag();
        org.junit.jupiter.api.Assertions.assertEquals(bag.TILES.size(), 97);
        //testing that the size increases with a new addition.
        bag.addTiles("_", 1);
        org.junit.jupiter.api.Assertions.assertEquals(bag.TILES.size(), 98);
    }

    @Test
    @DisplayName ("Testing drawTile")
    void drawTile() {
        TileBag bag = new TileBag();
        bag.drawTile();
        org.junit.jupiter.api.Assertions.assertEquals(bag.TILES.size(), 96);

    }

    @Test
    @DisplayName ("Testing isEmpty")
    void isEmpty() {
        TileBag bag = new TileBag();
        org.junit.jupiter.api.Assertions.assertFalse(bag.isEmpty());
    }

    @Test
    @DisplayName("Testing scoring tiles")
    void  scoringTiles() {
        TileBag bag = new TileBag();
        Tile testA = new Tile("A");
        Tile testD = new Tile("D");
        Tile testB = new Tile("B");
        Tile testF = new Tile("F");
        Tile testK = new Tile("K");
        Tile testJ = new Tile("J");
        Tile testQ = new Tile("Q");


        org.junit.jupiter.api.Assertions.assertEquals(1, bag.scoreLetter(testA.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(2, bag.scoreLetter(testD.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(3, bag.scoreLetter(testB.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(4, bag.scoreLetter(testF.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(5, bag.scoreLetter(testK.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(8, bag.scoreLetter(testJ.getLetter()));
        org.junit.jupiter.api.Assertions.assertEquals(10, bag.scoreLetter(testQ.getLetter()));
    }
}