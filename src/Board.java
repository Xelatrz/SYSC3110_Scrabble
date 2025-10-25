public class Board {
    public static final int SIZE = 15;
    private static Tile[][] grid =  new Tile[SIZE][SIZE]; //CHANGE THIS TO PRIVATE STATIC IN UML

    /**
     * Constructor for Board class.
     */
    public Board() {
    }

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

    public static void removeTile(int row, int col, Tile tile) { //Update UML
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

    public Tile getTile(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            System.out.println("Invalid coordinate");
            return null;
        }
        return grid[row][col];
    }
}
