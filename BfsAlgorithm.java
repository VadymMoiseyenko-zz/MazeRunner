import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Created by Vadman_PC on 19.06.2017.
 */
public class BfsAlgorithm implements AlgorithmManager {
    static char[][] currentMaze;
    public static int startY;
    public static int startX;
    public static int goalX;
    public static int goalY;

    public static List<String> path;

    @Override
    public List<String> getResolvePath(char[][] maze) {
        path = new ArrayList<>();
        currentMaze = maze;
        Node solution = getSolution();

        if (solution == null) {
            System.out.println("No solution");
        } else {
            System.out.println("Solution: ");
            solution.getSolutionCoords();
            System.out.println(solution.toString());
            return path;
        }

        return null;
    }

    @Override
    public void clearResolve() {
        path = new ArrayList<>();
        startX = Utils.EMPTY_BLOCK;
        startY = Utils.EMPTY_BLOCK;
        goalX = Utils.EMPTY_BLOCK;
        goalY = Utils.EMPTY_BLOCK;
    }

    public Node getSolution() {
        Queue<Node> list = new LinkedList<Node>();
        Queue<String> exploredCoords = new LinkedList<String>();

        getPosition(currentMaze);

        //Make an initial parent node of our first possible move
        Node initialNode = new Node(startY, startX, null);
        list.add(initialNode);

        while (!list.isEmpty()) {
            //Set this testNode equal to the first (FIFO) element and remove it
            Node testNode = list.remove();
            exploredCoords.add(testNode.coords);

            //Loop through all the move options
            for (Node childState : testNode.getChildren()) {
                //Check if this move option has been explored before
                if (!exploredCoords.contains(childState.coords) && !list.contains(childState)) {
                    //If we've reached goal, return solution
                    if (childState.reachedGoal()) {
                        testNode = null;
                        return childState;
                    }
                    //Otherwise, if the move option is a valid path (not a wall)
                    if (childState.isLegal()) {
                        //Add move option to list
                        list.add(childState);
                    }
                }
            }
        }
        return null;
    }

    void getPosition(char[][] currentMaze) {

        for (int i = 0; i < currentMaze.length; i++) {
            for (int j = 0; j < currentMaze[0].length; j++) {
                if (currentMaze[i][j] == Utils.GOAL_BLOCK) {
                    goalY = i;
                    goalX = j;
                } else if (currentMaze[i][j] == Utils.START_BLOCK) {
                    startY = i;
                    startX = j;
                }
            }
        }
    }
}
