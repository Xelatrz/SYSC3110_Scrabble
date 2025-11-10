import java.util.ArrayList;

/**
 * A Board models a scrabble game board, taking a 15 x 15 grid shape to allow players to
 * place tiles and create words.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 10/26/2025
 */
public class Board {
    /** The size of the grid */
    public static final int SIZE = 15;
    /** A 2-dimensional list of tiles, which is the specified grid size */
    Tile[][] grid =  new Tile[SIZE][SIZE];
    private Tile[][] tempGrid = new Tile[SIZE][SIZE];
    /** Starting space */
    public static final int CENTER = 8;


    /**
     * Constructs a new Board with no parameters, builds the empty grid.
     */
    public Board() {
    }

    /**
     * This method is called when a Player attempts to place a tile on the grid.
     * @param row An integer which corresponds to a row on the grid
     * @param col An integer which corresponds to a column on the grid
     * @param tile A Tile object which is pulled from the player's hand.
     */
    public void placeTile(int row, int col, Tile tile) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            System.out.println("Invalid coordinate");
            return;
        }
        if (grid[row][col] != null) {
            System.out.println("Tile already occupied!");
            return;
        }
        grid[row][col] = tile;
    }

    /**
     * This method is called when a player attempts to remove a tile.
     * @param row An integer which corresponds to a row on the grid
     * @param col An integer which corresponds to a column on the grid
     * @param tile A Tile object which is pulled from the player's hand.
     */
    public void removeTile(int row, int col, Tile tile) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            System.out.println("Invalid coordinate");
            return;
        }
        if (grid[row][col] == null) {
            System.out.println("Tile already empty");
            return;
        }
        grid[row][col] = null;
    }

    /**
     * This method prints the grid board and whichever tiles are placed on the grid.
     */
    public void display() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == null) {
                    System.out.print("-");
                }
                else {
                    System.out.print(grid[i][j].getLetter());
                }
            }
            System.out.println();
        }
    }

    /**
     * Finds the tile located at a specific row and column in the grid.
     * @param row An integer which corresponds to a row on the grid
     * @param col An integer which corresponds to a column on the grid
     * @return The tile located in the specified location on the game board.
     */
    public Tile getTile(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            System.out.println("Invalid coordinate");
            return null;
        }
        return grid[row][col] != null ? grid[row][col] : tempGrid[row][col];
    }

    /**
     * Determines whether the board is currently empty
     * @return True if the board is empty, false otherwise.
     */
    public boolean isEmpty() {
        for (int r = 0; r < this.SIZE; r++) {
            for (int c = 0; c < this.SIZE; c++) {
                if (this.grid[r][c] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeTempTile(int row, int col, Tile tile) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            return;
        }
        //space is already occupied
        if (grid[row][col] != null) {
            return;
        }
        tempGrid[row][col] = tile;
    }

    public void removeTempTile(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            return;
        }
        tempGrid[row][col] = null;
    }

    public void commitTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            grid[placedTile.row][placedTile.col] = placedTile.tile;
            tempGrid[placedTile.row][placedTile.col] = null;
        }
    }
    public void clearTempGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tempGrid[row][col] = null;
            }
        }
    }
}
