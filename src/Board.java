import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.Serializable;

/**
 * A Board models a scrabble game board, taking a 15 x 15 grid shape to allow players to
 * place tiles and create words.
 *
 * @author Cole Galway
 * @author Taylor Brumwell
 * @version 11/24/2025
 */
public class Board implements Serializable {
    /** The size of the grid */
    public static final int SIZE = 15;
    /** A 2-dimensional list of tiles, which is the specified grid size */
    private Tile[][] grid =  new Tile[SIZE][SIZE];
    /** A 2-dimensional list of temporary tiles. */
    private Tile[][] tempGrid = new Tile[SIZE][SIZE];

    public enum Premium {
        NORMAL, DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD, CENTER
    }

    private Premium[][] premiums = new Premium[SIZE][SIZE];

    /**
     * Constructs a new Board with no parameters, builds the empty grid.
     */
    public Board() {
        premiums = new Premium[SIZE][SIZE];
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                premiums[i][j] = Premium.NORMAL;
            }
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
        return tempGrid[row][col] != null ? tempGrid[row][col] : grid[row][col];
    }

    /**
     * Return the tile commited to the game board at a specified location
     * @param row The row of the placed tile
     * @param col The column of the placed tile
     * @return The tile placed at the specified location
     */
    public Tile getPermTile(int row, int col) {
        return grid[row][col];
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

    /**
     * Places a tile on the temporary grid
     * @param row An integer with the row where the tile is to be placed
     * @param col An integer with the column where the tile is to be placed
     * @param tile The tile that is to be placed on the temporary grid.
     */
    public boolean placeTempTile(int row, int col, Tile tile) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            return false;
        }
        //space is already occupied
        if (grid[row][col] != null) {
            return false;
        }
        tempGrid[row][col] = tile;
        return true;
    }

    /**
     * Places the tiles from the temporary grid into the main game grid.
     * @param placedTiles An arraylist of tiles placed in the turn.
     */
    public void commitTiles(ArrayList<PlacedTile> placedTiles) {
        for (PlacedTile placedTile : placedTiles) {
            grid[placedTile.row][placedTile.col] = placedTile.tile;
            tempGrid[placedTile.row][placedTile.col] = null;
        }
    }

    /**
     * Clears the temporary grid of all tiles.
     */
    public void clearTempGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tempGrid[row][col] = null;
            }
        }
    }

    /**
     * Find the current board pattern for a given line.
     * @param i the index of the line being scanned.
     * @param horizontal Whether the line is horizontal or not.
     * @return An array of Strings containing the pattern of the line on the board.
     */
    public String[] extractPattern(int i, boolean horizontal) {
        String[] pattern = new String[SIZE];
        for (int j = 0; j < SIZE; j++) {
            Tile tile;
            if (horizontal) {
                tile = getTile(i, j);
            } else {
                tile = getTile(j, i);
            }

            if (tile == null) {
                pattern[j] = "_";
            } else {
                pattern[j] = tile.getLetter();
            }
        }
        return pattern;
    }

    /**
     * Find all possible anchors on the line for a word to connect to.
     * Improvements would include making an anchor at the centre tile if the earlier players passed on their first turn (even though AIPlayer never plays first).
     * @param pattern The line pattern (including empty tiles).
     * @return A list of all anchors found (if any).
     */
    public List<Integer> findAnchors(String[] pattern) {
        List<Integer> anchors = new ArrayList<>();

        for (int i = 0; i < pattern.length; i++) {
            if (!pattern[i].equals("_")) {
                continue; //cannot place on existing tiles
            }

            boolean touching;
            if ((i > 0 && !pattern[i - 1].equals("_")) || (i < pattern.length - 1 && !pattern[i + 1].equals("_"))) {
                touching = true;
            } else {
                touching = false;
            }
            if (touching) {
                anchors.add(i);
            }
        }
        return anchors;
    }

    public void setPremium(int row, int col, Premium type) {
        premiums[row][col] = type;
    }

    public Premium getPremium(int row, int col) {
        return premiums[row][col];
    }

    public void setDefaultBoard() {
        // setting the default gameboard
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                premiums[i][j] = Premium.NORMAL;
                //triple word
                if ((i == 0 || i == 7 || i == 14) && (j == 0 || j == 7 || j == 14)) {
                    //center space
                    if (i == 7 && j == 7) {
                        premiums[i][j] = Premium.DOUBLE_WORD;
                    }
                    premiums[i][j] = Premium.TRIPLE_WORD;
                }

                //double word
                if (i == j || i + j == 14) {
                    if ((i == 0 && j == 0) || (i == 14 && j == 14) || (i == 0 && j == 14) || (i == 14 && j == 0)) {
                        premiums[i][j] = Premium.TRIPLE_WORD;
                    } else {
                        premiums[i][j] = Premium.DOUBLE_WORD;
                    }
                }

                //triple letter
                int[][] tripleLetter = {{1,5}, {1,9}, {5, 1}, {5,5}, {5,9}, {5, 13}, {9, 1}, {9, 5}, {9,9}, {9, 13}, {13, 5}, {13,9}};
                for (int[] p:  tripleLetter) {
                    if (p[0] == i && p[1] == j) {
                        premiums[i][j] = Premium.TRIPLE_LETTER;
                    }
                }
                //double letter
                int[][] doubleLetter = {{0,3}, {0,11}, {2,6}, {2,8}, {3,0}, {3,7}, {3, 14}, {6,2}, {6,6}, {6,8}, {6, 12}, {7,3}
                        ,{7,11}, {8,2}, {8,6}, {8,8}, {8,12}, {11, 0}, {11,7}, {11,14}, {12,6}, {12,8}, {14,3}, {14,11}};
                for (int[] p:doubleLetter) {
                    if (p[0] == i && p[1] == j) {
                        premiums[i][j] = Premium.DOUBLE_LETTER;
                    }
                }
            }
        }
    }
}
