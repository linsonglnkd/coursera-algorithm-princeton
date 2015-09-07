import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double [] thresholdArray;
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) 
            throw new java.lang.IllegalArgumentException("N or T too small");
        thresholdArray = new double[T];
        for (int i = 0; i < T; i++) thresholdArray[i] = simulation(N);
    }

    // help function to run the simulation, run simulation once
    // return the threshold (double) for this simulation
    private double simulation(int N) {
        int answer = 0;
        Percolation prec = new Percolation(N);
        while (! prec.percolates()) {
            int i = StdRandom.uniform(1, N+1);
            int j = StdRandom.uniform(1, N+1);
            if (! prec.isOpen(i,j)) {
                prec.open(i,j);
                answer++;
            }
        }
        return ((double) answer) / (N*N);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdArray);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(thresholdArray.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(thresholdArray.length);
    }

    // test client (described below)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(N, T);
        System.out.println(percStats.mean());
        System.out.println(percStats.stddev());
        System.out.println(percStats.confidenceLo());
        System.out.println(percStats.confidenceHi());
    }
}
