import java.util.LinkedList;
import java.util.List;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[] board1D;
    private int[][] board2D;
    private int ndim;
    private int zeroPosition;

    public Board(int[][] tiles) {

        if (tiles == null)
            throw new IllegalArgumentException();

        int j;
        ndim = tiles.length;
        board2D = tiles.clone();
        board1D = new int[ndim * ndim];


        for (int i = 0; i < tiles.length * tiles.length; i++) {
            j = i % tiles.length;
            board1D[i] = tiles[(i - j) / tiles.length][j];
            if (board1D[i] == 0) zeroPosition = i;
        }

    }

    // string representation of this board
    public String toString() {

        String s = board2D.length + "\n";
        for (int i = 0; i < board2D.length; i++) {
            for (int j = 0; j < board2D.length; j++)
                s += (board2D[i][j] + " ");
            s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return ndim;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < ndim * ndim; i++) {
            if (board1D[i] == 0)
                continue;
            if (board1D[i] != i + 1)
                hammingDistance++;
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        int[] goalCoordinates;
        int[] currCoordinates;
        for (int i = 0; i < ndim * ndim; i++) {

            if (board1D[i] == 0)
                continue;

            goalCoordinates = transform1Dto2D(board1D[i] - 1);
            currCoordinates = transform1Dto2D(i);
            manhattanDistance += (Math.abs(goalCoordinates[0] - currCoordinates[0])
                    + Math.abs(goalCoordinates[1] - currCoordinates[1]));
        }
        return manhattanDistance;

    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;

        if (this.dimension() != that.dimension()) return false;

        for (int i = 0; i < this.dimension() * this.dimension(); i++) {
            if (this.board1D[i] != that.board1D[i])
                return false;
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] neighbor = board1D.clone();
        int row, column;
        List<Board> neighbors = new LinkedList<>();

        int[] coordinates = transform1Dto2D(zeroPosition);
        row = coordinates[0];
        column = coordinates[1];

        if (!checkCollision(row - 1, column)) {
            exch(neighbor, zeroPosition, transform2Dto1D(row - 1, column));
            neighbors.add(new Board(transformArrayto2D(neighbor)));
            exch(neighbor, zeroPosition, transform2Dto1D(row - 1, column));
        }

        if (!checkCollision(row + 1, column)) {
            exch(neighbor, zeroPosition, transform2Dto1D(row + 1, column));
            neighbors.add(new Board(transformArrayto2D(neighbor)));
            exch(neighbor, zeroPosition, transform2Dto1D(row + 1, column));
        }

        if (!checkCollision(row, column - 1)) {
            exch(neighbor, zeroPosition, transform2Dto1D(row, column - 1));
            neighbors.add(new Board(transformArrayto2D(neighbor)));
            exch(neighbor, zeroPosition, transform2Dto1D(row, column - 1));
        }

        if (!checkCollision(row, column + 1)) {
            exch(neighbor, zeroPosition, transform2Dto1D(row, column + 1));
            neighbors.add(new Board(transformArrayto2D(neighbor)));
            exch(neighbor, zeroPosition, transform2Dto1D(row, column + 1));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[] twin = board1D.clone();
        int[] coordinates;
        int row, col, next;

        for (int i = 0; i < ndim * ndim; i++) {
            coordinates = transform1Dto2D(i);
            row = coordinates[0];
            col = coordinates[1];

            if (!checkCollision(row - 1, col)) {
                next = transform2Dto1D(row - 1, col);
                if (twin[next] != 0) {
                    exch(twin, i, next);
                    break;
                }
            }

            if (!checkCollision(row + 1, col)) {
                next = transform2Dto1D(row + 1, col);
                if (twin[next] != 0) {
                    exch(twin, i, next);
                    break;
                }
            }

            if (!checkCollision(row, col - 1)) {
                next = transform2Dto1D(row, col - 1);
                if (twin[next] != 0) {
                    exch(twin, i, next);
                    break;
                }
            }

            if (!checkCollision(row, col + 1)) {
                next = transform2Dto1D(row, col + 1);
                if (twin[next] != 0) {
                    exch(twin, i, next);
                    break;
                }
            }
        }

        return new Board(transformArrayto2D(twin));
    }

    private void exch(int[] board, int a, int b) {
        int temp = board[a];
        board[a] = board[b];
        board[b] = temp;
    }

    private int transform2Dto1D(int row, int column) {
        return row * ndim + column;
    }

    private int[][] transformArrayto2D(int[] arr) {
        int row, column;
        int[][] arr2D = new int[ndim][ndim];
        for (int i = 0; i < ndim * ndim; i++) {
            column = i % ndim;
            row = (i - column) / ndim;
            arr2D[row][column] = arr[i];
        }
        return arr2D;
    }

    private int[] transform1Dto2D(int index) {
        int column = index % ndim;
        int row = (index - column) / ndim;
        int[] coordinates = { row, column };
        return coordinates;
    }


    private boolean checkCollision(int row, int column) {
        if (row >= ndim || row < 0 || column >= ndim || column < 0)
            return true;
        return false;
    }


    public static void main(String[] args) {

    }
}
