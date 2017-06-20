package com.vadymMoiseyenko.mazeRunner;

/*
  class contained all constants and messages for user
 */
public class Utils {
    public static final char GOAL_BLOCK = 'G';
    public static final char START_BLOCK = 'S';
    public static final char EMPTY_BLOCK = ' ';
    public static final char PATH_MARKING = '0';
    public static final char WALL = 'X';

    public static final String AINSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final int MARGIN_70 = 70;

    public static final  int squareAreaForRectangleSize = 400;

    public static String intro = "<html>Hello. This is maze solver.<br> Please select start and end position by clicking to the empty box in maze </html>";
    public static String textNextMaze = "<html>We show new maze <br> Please select start and end position </html>";
    public static String textClearMaze = "<html>We refresh the maze  <br> Please select start and end position </html>";

}
