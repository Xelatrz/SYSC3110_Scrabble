/**
 * Test for the Player class
 *
 * @author Taylor Brumwell
 * @author Cole Galway
 * @version 11/24/2025
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AIPlayerTest {

    @BeforeEach
    protected void setUp() {
    }

    @AfterEach
    protected void tearDown() {
    }

    @Test
    @DisplayName ("Testing getting the AIPlayer name")
    public void getName() {
        AIPlayer ai1 = new AIPlayer("AI1", new GameModel());
        org.junit.jupiter.api.Assertions.assertEquals(ai1.getName(), "AI1");
    }

    @Test
    @DisplayName ("Testing getting the AIPlayer's current score")
    public void getScore() {
        //testing that the score of the player is 0
        AIPlayer ai1 = new AIPlayer("AI1", new GameModel());
        org.junit.jupiter.api.Assertions.assertEquals(ai1.getScore(), 0);

        //testing that the score can be changed
        ai1.setScore(25);
        org.junit.jupiter.api.Assertions.assertEquals(ai1.getScore(), 25);
    }

    @Test
    @DisplayName ("Testing if emptyhand returns correclty")
    public void emptyHand() {
        AIPlayer ai1 = new AIPlayer("AI1", new GameModel());
        TileBag bag = new TileBag();

        //testing that the hand has been emptied
        org.junit.jupiter.api.Assertions.assertTrue(ai1.emptyHand());
    }

    @Test
    @DisplayName("Testing that showHand prints properly")
    public void showHand() {
        AIPlayer ai1 = new AIPlayer("AI1", new GameModel());
        TileBag bag = new TileBag();
        ai1.fillHand(bag);
        ai1.showHand();

        //test that it should mirror
        System.out.println("Test Case");
        for (Tile tile: ai1.hand) {
            System.out.print(tile.getLetter() + ", ");
        }
        System.out.println();

    }

    @Test
    @DisplayName("Testing that drawing a single tile")
    public void drawTile() {
        AIPlayer ai1 = new AIPlayer("AI1", new GameModel());
        TileBag bag = new TileBag();
        ai1.drawTile(bag);

        org.junit.jupiter.api.Assertions.assertEquals(ai1.hand.size(), 1);
    }

    @Test
    @DisplayName ("Testing fillHand")
    public void fillHand() {
        AIPlayer ai1 = new AIPlayer ("AI1", new GameModel());
        TileBag bag = new TileBag();
        ai1.fillHand(bag);
        org.junit.jupiter.api.Assertions.assertEquals(ai1.hand.size(), 7);

        //testing it won't exceeed the max number of tiles
        ai1.drawTile(bag);
        org.junit.jupiter.api.Assertions.assertEquals(ai1.hand.size(), 7);
    }
}
