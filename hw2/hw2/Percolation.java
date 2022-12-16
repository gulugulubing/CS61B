package hw2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int scale;
    private int numOfOpen;
    private WeightedQuickUnionUF grid;
    private boolean[] open;
    private int[] min;
    private int[] max;
    private boolean isPercolation = false;
    public Percolation(int N) {
        grid = new WeightedQuickUnionUF(N * N);
        open = new boolean[N * N];
        min = new int[N * N];
        max = new int[N * N];
        scale = N;
        numOfOpen = 0;
        for (int i = 0; i < N * N; i++) {
            min[i] = i;
            max[i] = i;
        }
    }

    public void open(int row, int col) {
        if (col < 0 || col > scale - 1 || row < 0 || row > scale - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (!isOpen(row, col)) {
            int index = row * scale + col;
            open[index] = true;
            numOfOpen = numOfOpen + 1;
            unionOpen(row, col);
        }

        isFull(row, col); //will determine  isPercolation
    }

    /*union neighbors which are open*/
    private void unionOpen(int row, int col) {
        if (col < 0 || col > scale - 1 || row < 0 || row > scale - 1) {
            throw new IndexOutOfBoundsException();
        }
        int index = row * scale + col;
        int top;
        int bottom;
        int left;
        int right;
        if (row > 0) {
            top = (row - 1) * scale + col;
            if (open[top]) {
                unionAndFindMaxMin(index, top);
            }
        }
        if (row < scale - 1) {
            bottom = (row + 1) * scale + col;
            if (open[bottom]) {
                unionAndFindMaxMin(index, bottom);
            }
        }
        if (col > 0) {
            left = row * scale + col - 1;
            if (open[left]) {
                unionAndFindMaxMin(index, left);
            }
        }
        if (col < scale - 1) {
            right = row * scale + col + 1;
            if (open[right]) {
                unionAndFindMaxMin(index, right);
            }
        }
    }

    private void unionAndFindMaxMin(int p, int neighbor) {
        /*parents before union*/
        int parent1 = grid.find(p);
        int parent2 = grid.find(neighbor);

        grid.union(p, neighbor);
        /*notice:after union,parent1 may not equal grid.find[p]*/
        if (min[parent1] < min[parent2]) {
            min[grid.find(p)] = min[parent1];
        } else {
            min[grid.find(p)] = min[parent2];
        }
        if (max[parent1] < max[parent2]) {
            max[grid.find(p)] = max[parent2];
        } else {
            max[grid.find(p)] = max[parent1];
        }
    }

    public boolean isOpen(int row, int col) {
        if (col < 0 || col > scale - 1 || row < 0 || row > scale - 1) {
            throw new IndexOutOfBoundsException();
        }
        return open[row * scale + col];
    }

    /*
    public boolean isFull(int row, int col) {
        int index = row * scale + col;
        for(int i = 0; i < scale; i++) {
            if(grid.connected(index,i) && open[i] == true) {
                if (row == scale - 1) {
                    isPercolation = true;
                }
                return true;
            }
        }
        return false;
    }*/

    public boolean isFull(int row, int col) {
        if (col < 0 || col > scale - 1 || row < 0 || row > scale - 1) {
            throw new IndexOutOfBoundsException();
        }
        int index = row * scale + col;
        if (min[grid.find(index)] < scale && open[index]) {
            if (max[grid.find(index)] >= (scale - 1) * scale) {
                isPercolation = true;
            }
            return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return numOfOpen;
    }

    public boolean percolates() {
        return isPercolation;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation per = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            per.open(i, j);
        }
        System.out.println(per.percolates());
    }
}
