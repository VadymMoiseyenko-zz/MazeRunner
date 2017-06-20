package com.vadymMoiseyenko.mazeRunner.tool;

import com.vadymMoiseyenko.mazeRunner.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    class read mazes from .txt files
 */
public class ReadMazeFromDisc implements MazeProvider {


    private List<char[][]> mazes; //list with all mazes
    private char[][] copiedMaze;// first copy of maze first position
    private char[][] mazeForRestart; // second copy of maze first position

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


    private void readmazesList() {
        File folder = new File("./resource/Maze");
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
            // if something happen with resources we download cover maze and change text in messages
            mazes.add(coverMaze);
            Utils.intro = "<html>Sorry. Something happen with resource library. <br>So, we download cover maze for protection. <br>Please, read \"read me\" file.</html>";
            Utils.textNextMaze = "<html>Sorry. I can`t show next maze. Something happen with resource library. <br>So, we download cover maze for protection. <br>Please, read \"read me\" file.</html>";
        }

    }

    private char[][] buildArrayFromTextFile(File _mazeFile) {

        List<String> lines = new ArrayList<String>();

        try {
            Scanner sc = new Scanner(_mazeFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //store this line to string [] here
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Maze file not found");
        }

        //Calculate maze width and height
        int mazeWidth = lines.get(0).length();
        int mazeHeight = lines.size();

        //Convert to array
        char[][] array = new char[mazeHeight][mazeWidth];

        for (int i = 0; i < mazeHeight; i++) {
            array[i] = lines.get(i).toCharArray();
        }

        return array;
    }

    // get alleys first maze from list and set this maze to the end of list
    @Override
    public char[][] getMaze() {
        copyMazeFromList(mazes.get(0));
        mazes.add(mazes.get(0));
        mazes.remove(0);
        return copiedMaze;
    }

    // we two copy, special for restart
    public char[][] restartLastMaze() {
        copyMazeFromList(mazeForRestart);
        return copiedMaze;
    }

    // copy maze, because we don`t want to change original maze in list
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
