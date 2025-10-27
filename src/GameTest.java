/**
 * Test for the Game class
 *
 * @author Cole Galway
 * @version 10/27/2025
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("Testing adding a new player")
    void addPlayer() {
        Game game = new Game();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        game.addPlayer(p1);
        game.addPlayer(p2);

        org.junit.jupiter.api.Assertions.assertEquals(game.players.size(), 2);
    }
}