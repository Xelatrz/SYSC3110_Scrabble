import java.io.Serializable;

/**
 * PlacedTile models a specific tile once it has been placed on the board, storing the row, column
 * and tile information.
 *
 * @author Taylor Brumwell
 * @version 10/26/2025
 */
public class PlacedTile implements Serializable {
    /**
     * An integer representing the row where the tile is located.
     */
    public int row;
    /**
     * An integer representing the column where the tile is located.
     */
    public int col;
    /**
     * The tile which has been placed on the board from the player's hand.
     */
    public Tile tile;

    /**
     * Constructs a new PlacedTile.
     * @param row An integer which corresponds to a row on the grid
     * @param col An integer which corresponds to a column on the grid
     * @param tile A Tile object which is pulled from the player's hand
     */
    public PlacedTile(int row, int col, Tile tile) {
        this.row = row;
        this.col = col;
        this.tile = tile;
    }
}
