import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class SNode implements Comparable<SNode> {
	int two;
        Board value;
        SNode previous;
        int distance;

        SNode(Board value, SNode previous, int distance) {
            this.value = value;
            this.previous = previous;
            this.distance = distance;
        }

        SNode(Board value) {
            this.value = value;
            this.previous = null;
            this.distance = 0;
        }


        public int compareTo(SNode that) {

            if (this.getManhattan() + this.distance > that.getManhattan() + that.distance)
                return 1;
            if (this.getManhattan() + this.distance < that.getManhattan() + that.distance)
                return -1;
            return 0;
        }

        public int getDistance() {
            return this.distance;
        }

        public SNode getPrevious() {
            return this.previous;
        }

        public int getManhattan() {
            return value.manhattan();
        }

        public Boolean isGoal() {
            return value.isGoal();
        }

        public Iterable<Board> neighbors() {
            return value.neighbors();
        }

        public Stack<Board> getPath() {
            SNode iter = this;
            Stack<Board> path = new Stack<>();
            while (iter.getPrevious() != null) {
                path.push(iter.value);
                iter = iter.getPrevious();
            }
            path.push(iter.value);
            return path;
        }

    }

    private static MinPQ<SNode> main;
    private static MinPQ<SNode> twin;
    private boolean isSolvable;
    private Stack<Board> solution;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        isSolvable = false;
        solution = null;
        moves = -1;

        main = new MinPQ<SNode>(SNode::compareTo);
        twin = new MinPQ<SNode>(SNode::compareTo);

        main.insert(new SNode(initial));
        twin.insert(new SNode(initial.twin()));

        SNode nM;
        SNode nT;

        while (!main.isEmpty() && !twin.isEmpty()) {
            nM = main.delMin();
            nT = twin.delMin();

            if (nT.isGoal()) break;

            if (nM.isGoal()) {
                isSolvable = true;
                moves = nM.getDistance();
                solution = nM.getPath();

                break;
            }

            for (Board neigbhor : nM.neighbors()) {
                main.insert(new SNode(neigbhor, nM, nM.getDistance() + 1));
            }

            for (Board neigbhor : nT.neighbors()) {
                twin.insert(new SNode(neigbhor, nT, nT.getDistance() + 1));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }
}
