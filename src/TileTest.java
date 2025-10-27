import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        System.out.println(p1.hand.getFirst().letter);

    }
}