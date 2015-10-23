import java.util.ArrayList;

public class Board {
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private int N;
    private int [][] cells;
    // zeroRow and zeroCol record the position of the 0 cell
    private int zeroRow;
    private int zeroCol;

    public Board(int[][] blocks) {
        N = blocks.length;
        cells = new int[N][N];
        int k = 1;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                cells[i][j] = blocks[i][j];
                if (cells[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
    }

    // board dimension N
    public int dimension() {
        return N;
    }
    // number of blocks out of place
    public int hamming() {
        int k = 1;
        int hamDist = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (cells[i][j] != k++) hamDist++;
        hamDist--;
        return hamDist;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int result = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int value = cells[i][j];
                if (value == 0) continue;
                int correctRow = (cells[i][j] -1) / N;
                int correctCol = cells[i][j] - 1 - correctRow * N;
                result += Math.abs(i - correctRow) + Math.abs(j - correctCol);
                //System.out.println(value + ": " + correctRow + " " + correctCol);
            }
        return result;

    }
    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);

    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (N < 2) return null;
        int [][] twinCells = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                twinCells[i][j] = cells[i][j];
        if (zeroRow > 0) {
            twinCells[0][0] = cells[0][1];
            twinCells[0][1] = cells[0][0];
        }
        else {
            twinCells[1][0] = cells[1][1];
            twinCells[1][1] = cells[1][0];
        }
        return new Board(twinCells);

    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (this.cells[i][j] != that.cells[i][j])
                    return false;
        return true;

    }
    // help function: swap the zero cell with another cell
    private void swapZero(int row, int col) {
        int swap = cells[row][col];
        cells[row][col] = 0;
        cells[zeroRow][zeroCol] = swap;
        zeroRow = row;
        zeroCol = col;
        
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        // up
        if (zeroRow > 0) {
            swapZero(zeroRow - 1, zeroCol);
            neighbors.add(new Board(cells));
            swapZero(zeroRow + 1, zeroCol);  // switch back!
        }
        // down
        if (zeroRow < this.N - 1) {
            swapZero(zeroRow + 1, zeroCol);
            neighbors.add(new Board(cells));
            swapZero(zeroRow - 1, zeroCol);  // switch back!
        }
        // left
        if (zeroCol > 0) {
            swapZero(zeroRow, zeroCol - 1);
            neighbors.add(new Board(cells));
            swapZero(zeroRow, zeroCol + 1);  // switch back!
        }
        // right
        if (zeroCol < this.N - 1) {
            swapZero(zeroRow, zeroCol + 1);
            neighbors.add(new Board(cells));
            swapZero(zeroRow, zeroCol - 1);  // switch back!
        }
        return neighbors;
    }    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                s.append(String.format("%2d ", cells[i][j]));
            s.append("\n");
        }
        //s.append("manhattan: " + this.manhattan());
        //s.append(" hamming: " + this.hamming() + "\n");
        return s.toString();

    }
    // unit tests (not graded)
    public static void main(String[] args) {  
    }
}