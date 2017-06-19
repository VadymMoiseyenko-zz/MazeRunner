import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vadman_PC on 16.06.2017.
 */
public class ReadMazeFromDisc implements MazeProvider{

    private List<char[][]> mazes;
    char[][] copiedMaze;
    char[][] mazeForRestart;

    private char[][] coverMaze = new char[][]{ // cover maze, if something will happen when we download mazes from file
            {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
            {'X', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', 'X'},
            {'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X', 'X', ' ', 'X'},
            {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
            {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};


    public ReadMazeFromDisc() {
        readmazesList();
    }


    void readmazesList() {
        File folder = new File("./res/Maze");
        File[] files = folder.listFiles();
        mazes = new ArrayList<>();

        try {
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.contains(".txt")) {
                 mazes.add(buildArrayFromTextFile(file));
            }
        }
        } catch (NullPointerException e) {
            mazes.add(coverMaze);

        }

    }

    char[][] buildArrayFromTextFile (File _mazeFile) {

        List<String> lines = new ArrayList<String>();

        try {
            Scanner sc = new Scanner(_mazeFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //store this line to string [] here
                lines.add(line);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: Maze file not found");
        }

        //Calculate maze width and height
        int mazeWidth = lines.get(0).length();
        int mazeHeight = lines.size();

        //Convert to array
        char[][] array = new char[mazeHeight][mazeWidth];

        for(int i = 0; i < mazeHeight; i ++) {
            array[i] = lines.get(i).toCharArray();
        }

        return array;
    }


    @Override
    public char[][] getMaze() {
        copyMazeFromList(mazes.get(0));
        mazes.add(mazes.get(0));
        mazes.remove(0);
        return copiedMaze;
    }

    public char[][]  restartLastMaze() {
        copyMazeFromList(mazeForRestart);
        return copiedMaze;
    }

    private void copyMazeFromList(char[][] maze) {
        copiedMaze = new char[maze.length][maze[0].length];
        mazeForRestart = new char[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                copiedMaze[i][j] = maze[i][j];
                mazeForRestart[i][j] = maze[i][j];

            }
        }
    }
}
