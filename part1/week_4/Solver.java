import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;
// import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;

public class Solver {
    // search node
    private class Node {
        private Board board;   // the board
        private int moves;     // number of moves made to get to this board
        private Node prev;  // previous search node that leads to this board

        // constructor
        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
        public String toString() {
            StringBuilder s = new StringBuilder("");
            s.append(this.board.toString());
            int priority = this.board.manhattan() + moves;
            s.append("priorities: " + priority +"\n");
            s.append("moves: " + moves);
            return s.toString();
        }

        public int getMove() {
            return moves;
        }
    }

    // Comparator
    private static final Comparator<Node> BY_MANHATTAN = new ByMahattan();
    private static class ByMahattan implements Comparator<Node> {
        public int compare(Node a, Node b) {
            
            if (a.board.manhattan() + a.moves != b.board.manhattan() + b.moves)
                return (a.board.manhattan() + a.moves - b.board.manhattan() - b.moves);
            else
                return a.board.manhattan() - b.board.manhattan();
            
            //return (a.board.manhattan() + a.moves - b.board.manhattan() - b.moves);
        }
    }

    private static final Comparator<Node> BY_HAMMING = new ByHamming();
    private static class ByHamming implements Comparator<Node> {
        public int compare(Node a, Node b) {
            return (a.board.hamming() + a.moves - b.board.hamming() - b.moves);
        }
    }
    
    private boolean solvable;
    private ArrayList<Board> solution;    

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        int moves = 0;
        solution = new ArrayList<Board>();

        Node initialNode = new Node(initial, 0, null);
        Node initialTwinNode = new Node(initial.twin(), 0, null);
        MinPQ<Node> origPQ = new MinPQ(BY_MANHATTAN);
        MinPQ<Node> twinPQ = new MinPQ(BY_MANHATTAN);
        //ArrayList<Node> hold = new ArrayList<Node>();

        origPQ.insert(initialNode);
        twinPQ.insert(initialTwinNode);

        Node p1 = initialNode;
        Node p2 = initialTwinNode;

        while (!p1.board.isGoal() && !p2.board.isGoal()) {
            p1 = origPQ.delMin();
            //System.out.println(p1);
            //System.out.println(p1.board.isGoal());
            p2 = twinPQ.delMin();

            for (Board t : p1.board.neighbors()) {
                if (p1.prev != null && t.equals(p1.prev.board)) continue;
                origPQ.insert(new Node(t, p1.getMove() + 1, p1));
            }
            for (Board t : p2.board.neighbors()) {
                if (p2.prev != null && t.equals(p2.prev.board)) continue;
                twinPQ.insert(new Node(t, p2.getMove() + 1, p2));
            }
            //hold.add(p1);
            //hold.add(p2);
        }
        if (p1.board.isGoal()) {
            solvable = true;
            Stack<Node> reverseSolution = new Stack<Node>();
            do {
                reverseSolution.push(p1);
                p1 = p1.prev;
            } while (p1 != null);
            while (!reverseSolution.empty()) {
                solution.add(reverseSolution.pop().board);
            }
        }
        else {
            solvable = false;
            solution = null;
        }
    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return solution.size() - 1;
        else
            return -1;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }
    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        System.out.println("initial board:");
        System.out.println(initial);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}