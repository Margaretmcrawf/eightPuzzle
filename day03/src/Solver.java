/**
 * Solver definition for the 8 Puzzle challenge
 * Construct a tree of board states using A* to find a path to the goal
 */

import java.util.*;

public class Solver {

    public int minMoves = -1;
    private State solutionState;
    private State initialState;
    private boolean solved = false;

    /**
     * State class to make the cost calculations simple
     * This class holds a board state and all of its attributes
     */
    private class State implements Comparable<State> {
        // Each state needs to keep track of its cost and the previous state
        private Board board;
        private int moves;
        public int cost;
        private State prev;

        public State(Board board, int moves, State prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            cost = board.manhattan() + moves;
        }

        @Override
        public boolean equals(Object s) {
            if (s == this) return true;
            if (s == null) return false;
            if (!(s instanceof State)) return false;
            return ((State) s).board.equals(this.board);
        }

        /*
         * Return the cost difference between two states
         */
        @Override
        public int compareTo(State s) {
            return this.cost - s.cost;
        }
    }

    /*
     * Return the root state of a given state
     */
    private State root(State state) {
    	State r = state;

        while(state.prev != null){
            r = state.prev;
        }

        return r;
    }

    /*
     * A* Solver
     * Find a solution to the initial board using A* to generate the state tree
     * and a identify the shortest path to the the goal state
     */
    public Solver(Board initial) {
    	initialState = new State(initial, 0, null);
    	solution();
    }

    /*
     * Is the input board a solvable state
     */
    public boolean isSolvable() {
        return initialState.board.solvable();
    }

    /*
     * Return the sequence of boards in a shortest solution, null if unsolvable
     */
    public Iterable<Board> solution() {
        System.out.println("solution: ");

        if (!isSolvable()) {
            return null;
        }
        Set<State> open = new HashSet<>();
        Set<State> closed = new HashSet<>();

        open.add(initialState);

        while (open.size() != 0) {
            State minState = initialState;
            int minF = Integer.MAX_VALUE;

            for (State s : open) {
                if (s.cost < minF) {
                    minF = s.cost;
                    minState = s;
                }
            }
            open.remove(minState);
            State q = minState;

            for (Board uBoard : q.board.neighbors()) {
                State u = new State(uBoard, q.moves+1, q);

                if (uBoard.isGoal()) {
                    solved = true;
                    List<Board> path = new LinkedList<>();
                    solutionState = u;
                    State current = u;
                    minMoves = u.moves;
                    while (current != null) {
                        path.add(current.board);
                        current = current.prev;
                    }
                    return path;
                }

                boolean ignored = false;
                for (State n : open) {
                    if ((n.equals(u)) && (n.cost < u.cost)) {
                        ignored = true;
                    }
                } for (State n : closed) {
                    if ((n.equals(u)) && (n.cost < u.cost)) {
                        ignored = true;
                    }
                } if (!ignored) {
                    open.add(u);
                }
            }
            closed.add(q);
        }

        System.out.println("finished without solution");
        return null;
    }

    public State find(Iterable<State> iter, Board b) {
        for (State s : iter) {
            if (s.board.equals(b)) {
                return s;
            }
        }
        return null;
    }

    /*
     * Debugging space: Your solution can have whatever output you find useful
     * Optional challenge: create a command line interaction for users to input game
     * states
     */
    public static void main(String[] args) {
        int[][] initState = {{2, 3, 1}, {4, 5, 0}, {8, 6, 7}};
        Board initial = new Board(initState);

        // Solve the puzzle
        Solver solver = new Solver(initial);
        if (!solver.isSolvable())
            System.out.println("No solution");
        else {
            for (Board board : solver.solution()) {
                board.printBoard();
            }
            System.out.println(solver.minMoves);
        }
    }


}
