/**
 * Test for the Player class
 *
 * @author Cole Galway
 * @version 10/27/2025
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

class PlayerTest {

    @BeforeEach
    protected void setUp() {
    }

    @AfterEach
    protected void tearDown() {
    }

    @Test
    @DisplayName ("Testing getting the player name")
    public void getName() {
        Player p1 = new Player("Cole");
        org.junit.jupiter.api.Assertions.assertEquals(p1.getName(), "Cole");
    }

    @Test
    @DisplayName ("Testing getting the player's current score")
    public void getScore() {
        //testing that the score of the player is 0
        Player p1 = new Player("Cole");
        org.junit.jupiter.api.Assertions.assertEquals(p1.getScore(), 0);

        //testing that the score can be changed
        p1.setScore(3);
        org.junit.jupiter.api.Assertions.assertEquals(p1.getScore(), 3);
    }

    @Test
    @DisplayName ("Testing if emptyhand returns correclty")
    public void emptyHand() {
        Player p1 = new Player("Cole");
        TileBag bag = new TileBag();

        //testing that the hand has been emptied
        org.junit.jupiter.api.Assertions.assertTrue(p1.emptyHand());
    }

    @Test
    @DisplayName("Testing that showHand prints properly")
    public void showHand() {
        Player p1 = new Player("Cole");
        TileBag bag = new TileBag();
        p1.fillHand(bag);
        p1.showHand();

        //test that it should mirror
        System.out.println("Test Case");
        for (Tile tile: p1.hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();

    }

    @Test
    @DisplayName("Testing that drawing a single tile")
    public void drawTile() {
        Player p1 = new Player("Cole");
        TileBag bag = new TileBag();
        p1.drawTile(bag);

        org.junit.jupiter.api.Assertions.assertEquals(p1.hand.size(), 1);
    }

    @Test
    @DisplayName ("Testing fillHand")
    public void fillHand() {
        Player p1 = new Player ("Cole");
        TileBag bag = new TileBag();
        p1.fillHand(bag);
        org.junit.jupiter.api.Assertions.assertEquals(p1.hand.size(), 7);

        //testing it won't exceeed the max number of tiles
        p1.drawTile(bag);
        org.junit.jupiter.api.Assertions.assertEquals(p1.hand.size(), 7);
    }

}