import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class SNode implements Comparable<SNode> {
        Board value;
        SNode previous;
        int distance;
        int manhattan;

        SNode(Board value, SNode previous, int distance) {
            this.value = value;
            this.previous = previous;
            this.distance = distance;
            manhattan = value.manhattan();
        }

        SNode(Board value) {
            this.value = value;
            this.previous = null;
            this.distance = 0;
            manhattan = value.manhattan();
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
            return this.manhattan;
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

        MinPQ<SNode> main = new MinPQ<SNode>(SNode::compareTo);
        MinPQ<SNode> twin = new MinPQ<SNode>(SNode::compareTo);

        main.insert(new SNode(initial));
        twin.insert(new SNode(initial.twin()));

        SNode nM;
        SNode nT;

        while (!main.isEmpty() && !twin.isEmpty()) {
            nM = main.delMin();
            nT = twin.delMin();

            if (nT.value.isGoal()) break;

            if (nM.value.isGoal()) {
                isSolvable = true;
                moves = nM.getDistance();
                solution = nM.getPath();

                break;
            }

            SNode previous;
            for (Board neighbor : nM.neighbors()) {
                previous = nM.getPrevious();
                if ((previous == null)) {
                    main.insert(new SNode(neighbor, nM, nM.getDistance() + 1));
                }
                else {
                    
                    if (!neighbor.equals(previous.value))
                        main.insert(new SNode(neighbor, nM, nM.getDistance() + 1));
                }
            }

            for (Board neigbhor : nT.neighbors()) {
                previous = nT.getPrevious();
                if ((previous == null)) {
                    twin.insert(new SNode(neigbhor, nT, nT.getDistance() + 1));
                }
                else {
                    if (!neigbhor.equals(previous.value))
                        twin.insert(new SNode(neigbhor, nT, nT.getDistance() + 1));
                }
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
