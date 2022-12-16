package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int scale;
    private int numOfOpen;
    private WeightedQuickUnionUF grid;
    private boolean[] open;
    private int[] min;
    private boolean isPercolation = false;
    public Percolation(int N) {
        grid = new WeightedQuickUnionUF(N * N);
        open = new boolean[N*N];
        min = new int[N*N];
        scale = N;
        numOfOpen = 0;
        for (int i = 0; i < N*N; i++) {
            min[i] = i;
        }
    }

    public void open(int row, int col) {
        int index = row * scale + col;
        open[index] = true;
        numOfOpen = numOfOpen + 1;
        unionOpen(row, col);
    }

    private void unionOpen(int row, int col) {
        int index = row * scale + col;
        int top;
        int bottom;
        int left;
        int right;
        int parent1 = grid.find(index);
        int parent2;
        if (row > 0) {
            top = (row - 1) * scale + col;
            parent1 = grid.find(index);
            parent2 = grid.find(top);
            if (open[top] == true) {
                grid.union(index, top);
                if (min[parent1]<min[parent2]) {
                    min[grid.find(index)] = min[parent1];
                } else {
                    min[grid.find(index)] = min[parent2];
                }
            }
        }
        if (row < scale - 1) {
            bottom = (row + 1) * scale + col;
            parent1 = grid.find(index);
            parent2 = grid.find(bottom);
            if (open[bottom] == true) {
                grid.union(index, bottom);
                if (min[parent1]<min[parent2]) {
                    min[grid.find(index)] = min[parent1];
                } else {
                    min[grid.find(index)] = min[parent2];
                }
            }
        }
        if (col > 0) {
            left = row * scale + col - 1;
            parent1 = grid.find(index);
            parent2 = grid.find(left);
            if (open[left] == true) {
                grid.union(index, left);
                if (min[parent1]<min[parent2]) {
                    min[grid.find(index)] = min[parent1];
                } else {
                    min[grid.find(index)] = min[parent2];
                }
            }
        }
        if (col < scale - 1) {
            right = row * scale + col + 1;
            parent1 = grid.find(index);
            parent2 = grid.find(right);
            if (open[right] == true) {
                grid.union(index, right);
                if (min[parent1]<min[parent2]) {
                    min[grid.find(index)] = min[parent1];
                } else {
                    min[grid.find(index)] = min[parent2];
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
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
        int index = row * scale + col;
        if (min[grid.find(index)] < scale && open[index]) {
            if (row == scale - 1) {
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
}
