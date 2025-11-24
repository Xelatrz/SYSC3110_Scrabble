/**
 * Test for the Game class
 *
 * @author Cole Galway
 * @version 10/27/2025
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
}