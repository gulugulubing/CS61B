package hw2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.Stopwatch;


/* my way: use min[] and max[] (their index is set parent)
 to store the min and max of a joint set,if min of the set
 < N and max > (N-1)*N, it is percolated.
 my way's advantage is: need not face backwash problem!
 */

/*
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


    private void unionOpen(int row, int col) {
     //union neighbors which are open

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
        //parents before union
        int parent1 = grid.find(p);
        int parent2 = grid.find(neighbor);

        grid.union(p, neighbor);
        //notice:after union,parent1 may not equal grid.find[p]
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
 */

//teacher's way: To solve BackWash, it should create 2 instants of WUF.
// One has 2 additional virtual site (top and bottom),
// another has just one top virtual site just for isFull().
public class Percolation {
    private int scale;
    private int numOfOpen;
    // last 2 item is virtual, N*N - 1 is virtual top(open.length),connect to top line
    //  N*N is virtual bottom (open.length + 1) connect to bottom line
    private WeightedQuickUnionUF grid;
    // just add a virtual top(open.length), because isFull() should not use virtual bottom.
    private WeightedQuickUnionUF gridAvoidBackWash;

    //private QuickFindUF grid;
    //private UF grid;

    /*index's meaning same as WeightedQuickUnionUF*/
    private boolean[] open;
    public Percolation(int N) {
        grid = new WeightedQuickUnionUF(N * N + 2);
        gridAvoidBackWash = new WeightedQuickUnionUF(N * N + 1);
        //grid = new QuickFindUF(N*N);
        //grid = new UF(N*N);
        open = new boolean[N * N];
        scale = N;
        numOfOpen = 0;
    }

    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int index = row * scale + col;
            open[index] = true;
            numOfOpen = numOfOpen + 1;
            if (row < 1) {
                //connect to the top
                grid.union(index, open.length);
                gridAvoidBackWash.union(index, open.length);
            }
            if (row == (scale - 1)) {
                //connect to the bottom
                grid.union(index, open.length + 1);
            }
            unionOpen(row, col);
        }
    }

    /*connect the neighbour which is open*/
    private void unionOpen(int row, int col) {
        int index = row * scale + col;
        int top;
        int bottom;
        int left;
        int right;
        if (row > 0) {
            top = (row - 1) * scale + col;
            if(open[top]) {
                grid.union(index, top);
                gridAvoidBackWash.union(index, top);
            }
        }
        if (row < scale - 1) {
            bottom = (row + 1) * scale + col;
            if(open[bottom]) {
                grid.union(index, bottom);
                gridAvoidBackWash.union(index, bottom);
            }
        }
        if (col > 0) {
            left = row * scale + col - 1;
            if(open[left]) {
                grid.union(index, left);
                gridAvoidBackWash.union(index, left);
            }
        }
        if (col < scale - 1) {
            right = row * scale + col + 1;
            if(open[right]) {
                grid.union(index, right);
                gridAvoidBackWash.union(index, right);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        return open[row * scale + col];
    }

    public boolean isFull(int row, int col) {
        int index = row * scale + col;
        return  gridAvoidBackWash.connected(index, open.length);
    }

    public int numberOfOpenSites() {
        return numOfOpen;
    }

    public boolean percolates() {
        return grid.connected(open.length + 1, open.length);
    }

    public static void main(String[] args) {
        /*
        Stopwatch time = new Stopwatch();
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(200, 200, pf);
        System.out.println(ps.confidenceLow() + "  " + ps.confidenceHigh());
        System.out.println(time.elapsedTime());
        */
        In in = new In("inputFiles/input8.txt");
        int N = in.readInt();
        Percolation per = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            per.open(i, j);
            System.out.println(i + " " + j);
        }
        System.out.println(per.percolates());
    }
}
