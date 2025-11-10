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
}