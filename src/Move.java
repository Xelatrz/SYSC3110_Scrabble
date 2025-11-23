/**
 * Move is a specific play option for the AI, which is used in the process of
 * automatically determining and playing a legal word.
 *
 * @author Taylor Brumwell
 * @version 11/20/2025
 */

import java.util.*;

public class Move {
    public List<PlacedTile> placedTiles;
    public int score;

    public Move(List<PlacedTile> placedTiles, int score) {
        this.placedTiles = placedTiles;
        this.score = score;
    }
}
