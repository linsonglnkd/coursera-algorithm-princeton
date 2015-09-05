import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean [] siteMatrix;
    // when initialize, set the size of uf to be N*N+1; the last element is used as "root" to determine
    // whether a site is full.  this is to save the number of calls to WeightedQuickUnionUF so the program
    // runs faster
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufBottom;
    private boolean isPercolcated;
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new java.lang.IllegalArgumentException("size too small");
        this.N = N;
        siteMatrix = new boolean[N*N];
        uf = new WeightedQuickUnionUF(N*N+1);
        ufBottom = new WeightedQuickUnionUF(N*N+1);
        isPercolcated = false;
    }
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) 
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        if (siteMatrix[(i-1)*N+(j-1)]) return;
        // open the site
        siteMatrix[(i-1)*N+(j-1)] = true;
        // if this site is in the first row, then of course it is connected to the virtual "top" site
        if (i == 1) uf.union((i-1)*N+(j-1), N*N);

        if (i == N) ufBottom.union((i-1)*N+(j-1), N*N);
        // if left is open
        if (j > 1 && isOpen(i, j-1)) uf.union((i-1)*N+(j-1), (i-1)*N+(j-2));
        if (j > 1 && isOpen(i, j-1)) ufBottom.union((i-1)*N+(j-1), (i-1)*N+(j-2));
        // if right is open
        if (j < N && isOpen(i, j+1)) uf.union((i-1)*N+(j-1), (i-1)*N+j);
        if (j < N && isOpen(i, j+1)) ufBottom.union((i-1)*N+(j-1), (i-1)*N+j);
        // if up is open
        if (i > 1 && isOpen(i-1, j)) uf.union((i-1)*N+(j-1), (i-2)*N+(j-1));
        if (i > 1 && isOpen(i-1, j)) ufBottom.union((i-1)*N+(j-1), (i-2)*N+(j-1));
        // if down is open
        if (i < N && isOpen(i+1, j)) uf.union((i-1)*N+(j-1), i*N+(j-1));
        if (i < N && isOpen(i+1, j)) ufBottom.union((i-1)*N+(j-1), i*N+(j-1));

        if (!isPercolcated && uf.connected((i-1)*N+(j-1), N*N) && ufBottom.connected((i-1)*N+(j-1), N*N))
            isPercolcated = true;

    }
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) 
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        return siteMatrix[(i-1)*N+(j-1)];
    }
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        if (!isOpen(i, j)) return false;
        /* 
        old implementation, slow
        for (int k = 0; k < N; k++) 
            if (uf.connected((i-1)*N+(j-1), k)) return true;
        */
        return uf.connected((i-1)*N+(j-1), N*N);
    }
    // does the system percolate?    
    public boolean percolates() {
        /*
        for (int k = 1; k <= N; k++)
            if (isFull(N, k)) return true;
        return false;
        */
        return isPercolcated;
    }

    public static void main(String[] args) {
        // usage: java -cp XXXX Percolation test_file
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
