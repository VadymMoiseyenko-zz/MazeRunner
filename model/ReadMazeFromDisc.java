package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vadman_PC on 16.06.2017.
 */
public class ReadMazeFromDisc {

    public static final char goalBlock = 'G';
    public static final char startBlock = 'S';



    public ReadMazeFromDisc() {
        readmazesList();
    }

    private List<char[][]> mazes;

    public List<char[][]> getMazes() {
        return mazes;
    }
    void readmazesList() {
        File folder = new File("./src/Maze");
        File[] files = folder.listFiles();

        mazes = new ArrayList<>();

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.contains(".txt")) {
                 mazes.add(buildArrayFromTextFile(file));
            }
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

//            Find start and goal coordinates
//            for(int j = 0; j < mazeWidth; j ++) {
//                if(array[i][j] == goalBlock) {
//                    goalY = i;
//                    goalX = j;
//                }
//                else if(array[i][j] == startBlock) {
//                    startY = i;
//                    startX = j;
//                }
//            }
        }

        return array;
    }



}
