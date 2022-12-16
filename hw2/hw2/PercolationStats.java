package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] result;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        result = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation per = pf.make(N);
            while (!per.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (!per.isOpen(row, col)) {
                    per.open(row, col);
                }
                per.isFull(row, col);
            }
            result[i] = ((double) per.numberOfOpenSites()) / ((double) (N * N));
        }
    }

    public double mean() {
        return StdStats.mean(result);
    }

    public double stddev() {
        return StdStats.stddev(result);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(result.length);
    }

    public double confidenceHigh() {
        return  mean() + 1.96 * stddev() / Math.sqrt(result.length);
    }

}
