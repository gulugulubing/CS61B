package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;


public class Solver {
    private  class SearchNode implements Comparable<SearchNode>{
        private WorldState ws;
        private int moveNum;
        private int estimatedToGoal;
        private SearchNode previousNode;

        private SearchNode(WorldState initial) {
            ws = initial;
            moveNum = 0;
            estimatedToGoal = ws.estimatedDistanceToGoal();
            previousNode = null;
        }

        private SearchNode(WorldState current, int num, SearchNode pNode) {
            ws = current;
            moveNum = num;
            estimatedToGoal = ws.estimatedDistanceToGoal();
            previousNode = pNode;
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.moveNum + this.estimatedToGoal - o.moveNum - o.estimatedToGoal;
        }
    }
    private SearchNode lastNode;
    private MinPQ<SearchNode> minPQ;
    public Solver(WorldState initial) {
        int minimumMoves;
        lastNode = new SearchNode(initial);
        minPQ = new MinPQ<>();
        while (!lastNode.ws.isGoal()) {
            minimumMoves = lastNode.moveNum + 1;
            for(WorldState ws : lastNode.ws.neighbors()) {
                if (lastNode.previousNode == null || !ws.equals(lastNode.previousNode.ws)) {
                    SearchNode neighbor = new SearchNode(ws, minimumMoves, lastNode);
                    minPQ.insert(neighbor);
                }
            }
            lastNode = minPQ.delMin();
        }
    }

    public int moves() {
        return lastNode.moveNum;
    }
    public Iterable<WorldState> solution() {
        ArrayList<WorldState> sol = new ArrayList<>();
        WorldState[] worldStates = new WorldState[lastNode.moveNum + 1];
        int i = lastNode.moveNum;
        while (i > -1) {
            worldStates[i] = lastNode.ws;
            lastNode = lastNode.previousNode;
            i--;
        }
        for (i = 0; i < worldStates.length; i++) {
            sol.add(worldStates[i]);
        }
    return sol;
    }
}
