/**
 * Test for Tile class
 *
 * @author Cole Galway
 * @version 10/27/2025
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TileTest {

    @Test
    @DisplayName ("Testing that the correct letter is returned")
    void getLetter() {
        Player p1 = new Player("Cole");
        TileBag bag = new TileBag();
        p1.drawTile(bag);

        p1.hand.getFirst().getLetter();
        //test to mirror result
        System.out.println("Test Case");
        System.out.println(p1.hand.getFirst().getLetter());

    }

    @Test
    @DisplayName ("Testing blank tile")
    void testBlankTileLetter() {
        Player p1 = new Player("Cole");
        Tile blank = new Tile("-");
        org.junit.jupiter.api.Assertions.assertEquals("-", blank.getLetter());
    }

    @Test
    @DisplayName ("Testing blank tile score")
    void testBlankTileScore() {
        TileBag bag = new TileBag();
        Tile blank = new Tile("-");
        bag.scoreLetter(blank.getLetter());
        org.junit.jupiter.api.Assertions.assertEquals(0, bag.scoreLetter(blank.getLetter()));
    }

    @Test
    @DisplayName("Testing assigning a new value to blank tiles")
    void testAssigningBlankTiles() {
        Player p1 = new Player("Cole");
        Tile blank = new Tile("-");
        blank.setLetter("B");
        org.junit.jupiter.api.Assertions.assertEquals("B", blank.getLetter());
    }

}