import java.util.LinkedList;
import java.util.Queue;

public class Node {

    int x;
    int y;
    String coords;
    Node parentNode;



    public Node(int myY, int myX, Node parent) {
        this.y = myY;
        this.x = myX;
        this.coords = myY+","+myX;
        parentNode = parent;
    }

    public Queue<Node> getChildren() {
        Node n = this;
        Queue<Node> children = new LinkedList<Node>();

        //Right
        children.add(new Node(n.y, n.x + 1, n));
        //Left
        children.add(new Node(n.y, n.x - 1, n));
        //Up
        children.add(new Node(n.y - 1, n.x, n));
        //Down
        children.add(new Node(n.y + 1, n.x, n));

        return children;
    }

    //Recursively return solution
    public void getSolutionCoords() {
        Traverser.path.add(coords);
        if(parentNode != null) {
            parentNode.getSolutionCoords();
        }

    }

    //Check if we've reached goal
    public boolean reachedGoal() {
        return x == Traverser.goalX && y == Traverser.goalY;
    }

    public boolean isLegal() {
        if(y >= 0 && y < Traverser.currentMaze.length) {
            if(x >= 0 && x < Traverser.currentMaze[0].length) {
                return Traverser.currentMaze[y][x] == Traverser.emptyBlock || Traverser.currentMaze[y][x] == Traverser.pathMarking;
            }
        }
        return false;
    }
}