import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    int N;
    boolean [] siteMatrix;
    WeightedQuickUnionUF uf;
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new java.lang.IllegalArgumentException("size too small");
        this.N = N;
        siteMatrix = new boolean[N*N];
        uf = new WeightedQuickUnionUF(N*N);
    }
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) 
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        siteMatrix[(i-1)*N+(j-1)] = true;
        // if left is open
        if (j > 1 && isOpen(i, j-1)) uf.union((i-1)*N+(j-1), (i-1)*N+(j-2));
        // if right is open
        if (j < N && isOpen(i, j+1)) uf.union((i-1)*N+(j-1), (i-1)*N+j);
        // if up is open
        if (i > 1 && isOpen(i-1, j)) uf.union((i-1)*N+(j-1), (i-2)*N+(j-1));
        // if down is open
        if (i < N && isOpen(i+1, j)) uf.union((i-1)*N+(j-1), i*N+(j-1));
    }
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) 
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        return siteMatrix[(i-1)*N+(j-1)];
    }
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        for (int k = 0; k < N; k++) 
            if (uf.connected((i-1)*N+(j-1), k)) return true;
        return false;
    }
    // does the system percolate?    
    public boolean percolates() {
        for (int k = 1; k <= N; k++)
            if (isFull(N, k)) return true;
        return false;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int N = in.readInt();         // N-by-N percolation system
        System.out.println(N);

        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        System.out.println(perc.percolates());
    }
}
