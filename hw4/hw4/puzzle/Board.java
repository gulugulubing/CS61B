package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] t;
    private int size;
    public Board(int[][] tiles) {
        t = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                t[i][j] = tiles[i][j];
            }
        }
        size = tiles.length;
    }

    public int tileAt(int i, int j) {
        if (i > t.length - 1 || i < 0 || j > t.length - 1 || j < 0) {
            throw new IndexOutOfBoundsException();
        }
        return t[i][j];
    }

    public  int size() {
        return size;
    }
    @Override
    //http://joshh.ug/neighbors.html
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }


    public int hamming() {
        int ham = 0;
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                if (t[i][j] == 0) {
                    continue;
                }
                if (t[i][j] != t.length * i + j + 1) {
                    ham++;
                }
            }
        }
        return ham;
    }

    public int manhattan() {
        int m = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (t[i][j] == 0) {
                    continue;
                }
                if (t[i][j] != size * i + j + 1) {
                    m = m + Math.abs(row(t[i][j]) - row(size * i + j + 1))
                            + Math.abs(colomn(t[i][j]) - colomn(size * i + j + 1));
                }
            }
        }
        return m;
    }

    private int row(int s) {
        return (s - 1) / size;
    }

    private int colomn(int s) {
        return (s - 1) % size;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board by = (Board) y;
        if (this.size != by.size) {
            return false;
        }

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.t[i][j] != by.t[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
