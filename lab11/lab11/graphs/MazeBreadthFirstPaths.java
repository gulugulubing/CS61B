package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> fringe = new ArrayDeque<>();
        fringe.add(s);
        marked[s] = true;
        announce();
        while (!fringe.isEmpty()) {
            int v = fringe.remove();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (marked[w] == false) {
                    fringe.add(w);
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    announce();
                }
            }
            /* return here may be better, but not as same as teacher's video
            if (v == t) {
                return;
            }
             */
        }


        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
    }


    @Override
    public void solve() {
        bfs();
    }
}

