package assignment2;

/**
 * Represents the playing board.
 *
 * @author Hendrik Werner // s4549775
 * @author Jasper Haasdijk // s4449754
 */
public class Board {
    public final int width;
    public final int height;
    private final Position initialKnightPosition;
    private final boolean[][] visited;
    private final Move[] history;
    private Position knightPosition;
    
    /**
     * Constructor method.
     * 
     * @param width Width of the board.
     * @param height Height of the board.
     * @param start Starting position of the knight.
     */
    public Board (int width, int height, Position start) {
        this.width = width;
        this.height = height;
        this.initialKnightPosition = start;
        this.knightPosition = start;
        this.visited = new boolean[width][height];
        this.visited[start.x][start.y] = true;
        this.history = new Move[width * height - 1];
    }
    
    /**
     * Getter method for the knight's position.
     * 
     * @return The position of the knight.
     */
    public Position getCurrentPosition () {
        return knightPosition;
    }
    
    /**
     * Get the position of the knight after move m has been applied.
     * 
     * @param m The move to apply to the knight.
     * @return The new position of the knight after applying move m.
     */
    public Position getNewPosition (Move m) {
        return new Position(knightPosition.x + m.x,
                            knightPosition.y + m.y);
    }
    
    /**
     * Apply move m to the board.
     * 
     * @param m The move to apply.
     */
    public void apply (Move m) {
        this.knightPosition = getNewPosition(m);
        this.visited[knightPosition.x][knightPosition.y] = true;
    }
    
    /**
     * Revert move m.
     * 
     * @param m The move to revert.
     */
    public void revert (Move m) {
        this.visited[knightPosition.x][knightPosition.y] = false;
        this.knightPosition = getNewPosition(m.invert());
    }
    
    /**
     * Check if position p has been visited by the knight.
     * 
     * @param p The position to check.
     * @return Whether or not the knight has already visited position p.
     */
    public boolean isVisited (Position p) {
        return visited[p.x][p.y];
    }
    
    /**
     * Public interface for solving.
     * 
     * @return A history of moves that lead to a solution.
     */
    public Move[] solve () {
        this.solve(history, 1, width * height - 1);
        return history;
    }
    
    /**
     * Turn the board into a string.
     * 
     * @return A string representing the board.
     */
    @Override
    public String toString () {
        String s = "";
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (new Position(x, y).equals(knightPosition)) {
                    s += "(" + visited[x][y] + ")\t";
                } else {
                    s += visited[x][y] + "\t";
                }
            }
            s += "\n";
        }
        return s;
    }
    
    /**
     * Solve the puzzle using backtracking.
     * 
     * @param history History of moves that lead to the current state.
     * @param step Number of the current step.
     * @param notVisited Number of fields not yet visited.
     * @return Whether or not there is a solution to the current state.
     */
    private boolean solve (Move[] history, int step, int notVisited) {
        if (notVisited == 0) {
            return true;
        }
        for (Move moveCandidate: Move.getKnightMoves()) {
            if (moveCandidate.applicableTo(this)) {
                this.apply(moveCandidate);
                if (this.solve(history, step + 1, notVisited - 1)) {
                    history[step - 1] = moveCandidate;
                    return true;
                }
                this.revert(moveCandidate);
            }
        }
        return false;
    }
}