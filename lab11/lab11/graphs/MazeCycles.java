package lab11.graphs;
import edu.princeton.cs.algs4.Stack;
/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        int sourceX = 1;
        int sourceY = 1;
        int targetX = maze.N();
        int targetY = maze.N();
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = -1;
        edgeTo[s] = s;
        dfs(s);
    }

    //use a stack not recursion to DEPTH FIRST PATH
    private void dfs(int v) {
        Stack<Integer> stack = new Stack();
        int previous = 0;
        int current;
        stack.push(v);
        while (!stack.isEmpty()) {
            announce();
            current = stack.pop();
            if (!marked[current]) {
                marked[current] = true;
                distTo[current] = distTo[previous] + 1;
            }
            else {
                //delete none-circle edge
                for(int i = 0 ; i < maze.V() ; i++) {
                    if (distTo[i] < distTo[current]) {
                        edgeTo[i] = i;
                    }
                }
                announce();
                break;
            }
            for (int w : maze.adj(current)) {
                if ( w != edgeTo[current] ) {
                    edgeTo[w] = current;
                    stack.push(w);
                }
            }
            previous = current;
        }
    }

}

