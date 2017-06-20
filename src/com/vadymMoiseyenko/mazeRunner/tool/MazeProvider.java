package com.vadymMoiseyenko.mazeRunner.tool;

public interface MazeProvider {
    char[][] getMaze();
     char[][] restartLastMaze();
}
