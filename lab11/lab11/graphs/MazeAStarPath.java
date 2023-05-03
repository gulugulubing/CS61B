package lab11.graphs;
import edu.princeton.cs.algs4.MinPQ;
/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    private int targetX, targetY;

    private class Node implements Comparable<Node> {
        private int v;
        private int priority;

        public Node(int v) {
            this.v = v;
            this.priority = distTo[v] + h(v);
        }

        @Override
        public int compareTo(Node o) {
            return this.priority - o.priority;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        return  Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        Node currentNode = new Node(s);
        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(currentNode);
        marked[currentNode.v] = true;
        while (!minPQ.isEmpty()) {
            currentNode = minPQ.delMin();
            marked[currentNode.v] = true;
            announce();
            for(int w : maze.adj(currentNode.v)) {
                if (marked[w] == false) {
                    distTo[w] = distTo[currentNode.v] + 1;
                    edgeTo[w] = currentNode.v;
                    if (w == t) {
                        marked[w] = true;
                        announce();
                        return;
                    } else {
                        minPQ.insert(new Node(w));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

