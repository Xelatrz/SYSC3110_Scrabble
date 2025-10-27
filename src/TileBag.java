import java.util.*;

/**
 * This class models the bag of tiles in a standard scrabble game.
 *
 * @author Cole Galway
 * @version 10/26/2025
 */
public class TileBag {
    private final List <Character> tiles = new ArrayList<>();
    private final Random rand = new Random();

    public TileBag() {
        addTiles('E', 12);
        addTiles('A', 9);
        addTiles('I', 9);
        addTiles('O', 8);
        addTiles('N', 6);
        addTiles('R', 6);
        addTiles('T', 6);
        addTiles('L', 4);
        addTiles('S', 4);
        addTiles('U', 4);
        addTiles('D', 4);
        addTiles('G', 3);
        addTiles('B', 2);
        addTiles('C', 2);
        addTiles('M', 2);
        addTiles('P', 2);
        addTiles('F', 2);
        addTiles('H', 2);
        addTiles('V', 2);
        addTiles('W', 2);
        addTiles('Y',2);
        addTiles('K', 1);
        addTiles('J', 1);
        addTiles('Q', 1);
        addTiles('Z', 1);
    }

    private void addTiles(char letter, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(letter);
        }

    }

    /**
     * Returns the Tile which has been drawn from the bag.
     * @return Tile which has been selected at random from the bag
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null; //no tiles are left in the bag
        }
        char letter = tiles.remove(rand.nextInt(tiles.size()));
        return new Tile(String.valueOf(letter));
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}
