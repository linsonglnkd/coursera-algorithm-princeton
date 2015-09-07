import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.*;

public class Percolation {
    private int N;
    private boolean [] siteMatrix;
    // a better vesion, only use one uf for the top
    // build top other matrix, one to record the "root's" status to connect to the top
    // another to store the "root's" status to connect to the bottom
    private WeightedQuickUnionUF uf;
    private boolean [] connectToTop;
    private boolean [] connectToBottom;
    private boolean isPercolcated;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) throw new java.lang.IllegalArgumentException("size too small");
        this.N = N;
        siteMatrix = new boolean[N*N];
        connectToTop = new boolean[N*N];
        connectToBottom = new boolean[N*N];
        uf = new WeightedQuickUnionUF(N*N+1);
        isPercolcated = false;
    }
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) 
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        if (siteMatrix[(i-1)*N+(j-1)]) return;
        // open the site
        siteMatrix[(i-1)*N+(j-1)] = true;
        // find the root of the current site
        int rootCurrent = uf.find((i-1)*N+(j-1));
        // if this site is in the first row, then of course it is connected to the virtual "top" site
        // also update the connnecToTop array (store's the status of possible roots)
        if (i == 1) { 
            uf.union((i-1)*N+(j-1), N*N);
            connectToTop[rootCurrent] = true;
        }
        if (i == N) {
            connectToBottom[rootCurrent] = true;
        }
        // let the left, right, up, down root be the current site, and change them properly if they do exist
        int rootLeft = rootCurrent;
        int rootRight = rootCurrent;
        int rootUp = rootCurrent;
        int rootDown = rootCurrent;
        if (j > 1 && isOpen(i, j-1)) rootLeft = uf.find((i-1)*N+(j-2));
        if (j < N && isOpen(i, j+1)) rootRight = uf.find((i-1)*N+j);
        if (i > 1 && isOpen(i-1, j)) rootUp = uf.find((i-2)*N+(j-1));
        if (i < N && isOpen(i+1, j)) rootDown = uf.find(i*N+(j-1));

        // if left is open
        if (j > 1 && isOpen(i, j-1)) uf.union((i-1)*N+(j-1), (i-1)*N+(j-2));
        // if right is open
        if (j < N && isOpen(i, j+1)) uf.union((i-1)*N+(j-1), (i-1)*N+j);
        // if up is open
        if (i > 1 && isOpen(i-1, j)) uf.union((i-1)*N+(j-1), (i-2)*N+(j-1));
        // if down is open
        if (i < N && isOpen(i+1, j)) uf.union((i-1)*N+(j-1), i*N+(j-1));

        // update the root status
        if (connectToTop[rootCurrent] || connectToTop[rootLeft] || connectToTop[rootRight] 
            || connectToTop[rootUp] || connectToTop[rootDown])
        {
            connectToTop[rootCurrent] = true;
            connectToTop[rootLeft] = true;
            connectToTop[rootRight] = true;
            connectToTop[rootUp] = true;
            connectToTop[rootDown] = true;
        }
        if (connectToBottom[rootCurrent] || connectToBottom[rootLeft] || connectToBottom[rootRight] 
            || connectToBottom[rootUp] || connectToBottom[rootDown])
        {
            connectToBottom[rootCurrent] = true;
            connectToBottom[rootLeft] = true;
            connectToBottom[rootRight] = true;
            connectToBottom[rootUp] = true;
            connectToBottom[rootDown] = true;
        }

        // change the percolate flag
        if (!isPercolcated && connectToTop[rootCurrent] && connectToBottom[rootCurrent]) 
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
        return uf.connected((i-1)*N+(j-1), N*N);
    }
    // does the system percolate?    
    public boolean percolates() {
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
