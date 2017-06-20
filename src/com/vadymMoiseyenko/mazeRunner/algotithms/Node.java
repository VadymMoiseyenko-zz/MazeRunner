package com.vadymMoiseyenko.mazeRunner.algotithms;

import com.vadymMoiseyenko.mazeRunner.Utils;

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
        BfsAlgorithm.path.add(coords);
        if(parentNode != null) {
            parentNode.getSolutionCoords();
        }

    }

    //Check if we've reached goal
    public boolean reachedGoal() {
        return x == BfsAlgorithm.goalX && y == BfsAlgorithm.goalY;
    }

    public boolean isLegal() {
        if(y >= 0 && y < BfsAlgorithm.currentMaze.length) {
            if(x >= 0 && x < BfsAlgorithm.currentMaze[0].length) {
                return BfsAlgorithm.currentMaze[y][x] == Utils.EMPTY_BLOCK || BfsAlgorithm.currentMaze[y][x] == Utils.PATH_MARKING;
            }
        }
        return false;
    }
}