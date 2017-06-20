package com.vadymMoiseyenko.mazeRunner;

import com.vadymMoiseyenko.mazeRunner.algotithms.AlgorithmManager;
import com.vadymMoiseyenko.mazeRunner.algotithms.BfsAlgorithm;
import com.vadymMoiseyenko.mazeRunner.tool.MazeProvider;
import com.vadymMoiseyenko.mazeRunner.tool.ReadMazeFromDisc;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MazeRunner extends JFrame {
    private JButton solveStepByStep; // button which solve maze step-by-step
    private JButton solve; // button which solve maze by on click
    private JButton clear; // button which clear maze from start position, end position and path
    private JButton exit; // button for going out from program
    private JButton nextMaze; // show new maze
    private JLabel message;  // message to the user


    private MazeProvider mMazeProvider; // maze provider read files from .txt documents and give maze. It implements by interface,
    // because if in future we want to change way hove to get mazes, it will be easier to do it
    private AlgorithmManager mAlgorithmManager; // mAlgorithmManager core of business logic. It  implements by interface, because
    // if we want to add new algorithms we already have some kind of API which easy to expand and export
    private List<String> pathCoordinate;  // temp place, where saved all resolved coordinates from mAlgorithmManager

    private char[][] currentMaze; // temp place, where saved maze from mMazeProvider for displaying.

    private int dynamicDimentionHelper;
    private int manualClick; // if we want to solve maze step-by-step, this variable save manual iteration by clicking to the button
    private boolean firstAsk = true;
    private boolean haveStart = false;
    private boolean haveEnd = false;
    private boolean isMazeSolved = false;


    public static void main(String[] args) {
        new MazeRunner();
    }

    MazeRunner() {
        mMazeProvider = new ReadMazeFromDisc();
        mAlgorithmManager = new BfsAlgorithm();
        pathCoordinate = new ArrayList<>();
        currentMaze = mMazeProvider.getMaze();
        printMazeInConsole(currentMaze); //print maze in console
        setViews();
        message.setText(Utils.intro);

    }

    void printMazeInConsole(char[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            System.out.println();
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j]);
            }
        }
        System.out.println("\n");
    }

    // adding solved coordinates to maze and Print path in console
    void printSolution(List<String> pathCoordinate) {
        for (int i = 0; i < currentMaze.length; i++) {
            System.out.println();
            for (int j = 0; j < currentMaze[0].length; j++) {
                String currentCoords = i + "," + j;
                if (pathCoordinate.contains(currentCoords) && currentMaze[i][j] != Utils.START_BLOCK && currentMaze[i][j] != Utils.GOAL_BLOCK) {
                    System.out.print(Utils.ANSI_RED + Utils.PATH_MARKING + Utils.AINSI_BLACK);
                    currentMaze[i][j] = Utils.PATH_MARKING; //adding solved coordinates to maze
                } else {
                    System.out.print(currentMaze[i][j]);
                }
            }
        }
        System.out.println("\n");
        repaint(); // repaint maze
    }

    /*
     This method will draw solution step by step, by clicking button.
     */
    private void printStepByStepSolution() {
        if (manualClick <= pathCoordinate.size() - 1) {
            String s = pathCoordinate.get(manualClick);
            String[] split = s.split(",");
            int xPos = Integer.parseInt(split[0]);
            int yPos = Integer.parseInt(split[1]);
            if (currentMaze[xPos][yPos] != 'S' && currentMaze[xPos][yPos] != 'G') {
                currentMaze[xPos][yPos] = Utils.PATH_MARKING;
            }
            manualClick++; // iteration provided by clicking
            repaint();

            if (!(pathCoordinate.size() == manualClick)) {
                message.setText("<html>You want to solve the maze step-by-step.<br> You have to make " + (pathCoordinate.size() - manualClick) + " steps</html>");
            } else {
                solve.setEnabled(false);
                solveStepByStep.setEnabled(false);
                message.setText("<html>Congrats! Maze was solved after " + pathCoordinate.size() + " steps </html>");
            }
        }
    }

    void setViews() {
        setTitle("Maze");     //Title For JFrame
        setSize(960, 530);    // Size For JFrame  (width,height)

        setLocationRelativeTo(null);// to make JFrame appear in the Middle of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // To close the app when click on exit or (X)
        setLayout(null); // to set the position of the components (like JLabel and JTextField, Buttons) and by hand (we will choose the position by ourselves)

        // initialize objects for Buttons
        solveStepByStep = new JButton("Step-by-step");
        solveStepByStep.setEnabled(false);
        solve = new JButton("Solve");
        solve.setEnabled(false);
        clear = new JButton("Clear");
        exit = new JButton("Exit");
        nextMaze = new JButton("Next Maze");
        message = new JLabel("message");
        message.setForeground(Color.blue);
        message.setFont(new Font("Helvetica", Font.PLAIN, 16));
        Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        message.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

        // Add The Buttons to JFrame
        add(solveStepByStep);
        add(solve);
        add(clear);
        add(exit);
        add(nextMaze);
        add(message);

        //set viability
        setVisible(true);

        // set the positions of the components on the JFrame (x,y,width,height). here we chose the position by hand, this is why we sat the set Layout to null
        message.setBounds(500, 180, 400, 80);
        solveStepByStep.setBounds(500, 50, 100, 40);
        solve.setBounds(630, 50, 100, 40);
        clear.setBounds(760, 50, 100, 40);
        exit.setBounds(760, 115, 100, 40);
        nextMaze.setBounds(500, 115, 230, 40);

        //set listener to mouse clicking
        MouseHandler listener = new MouseHandler();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        //set listener to solve button clicking
        solve.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                pathCoordinate = mAlgorithmManager.getResolvePath(currentMaze); // get list with solution
                if (pathCoordinate == null) {
                    System.out.println("No solution");
                    message.setText("Sorry, Maze don`t have solution");
                } else {
                    System.out.println("Solution: ");
                    printSolution(pathCoordinate);// print solution
                    isMazeSolved = true;
                    message.setText("<html>Maze was solved, after " + (pathCoordinate.size() - 1) + " steps <br> Now you can press button \"clear\" or button \"next maze\" <html>");
                    solve.setEnabled(false);
                    solveStepByStep.setEnabled(false);
                }

            }
        });

        //set listener to nextMaze button clicking
        nextMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMaze = mMazeProvider.getMaze(); // receive new maze
                restartHelperVar(); //reset all helper variable to default position
                repaint();
                message.setText(Utils.textNextMaze);
            }
        });

        //set listener to clear button clicking
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMaze = mMazeProvider.restartLastMaze(); // receive recovered current maze
                restartHelperVar();  //reset all helper variable to default position
                repaint();
                message.setText(Utils.textClearMaze);
            }
        });

        //set listener to exit button clicking
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);        //Close The App
            }
        });

        //set listener to solveStepByStep button clicking
        solveStepByStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstAsk) { // checking
                    pathCoordinate = mAlgorithmManager.getResolvePath(currentMaze);

                    if (pathCoordinate == null) {
                        System.out.println("No solution");
                        message.setText("Sorry, Maze don`t have solution");
                    } else {
                        System.out.println("Solution: ");
                        message.setText("<html>Maze will be solved, after " + (pathCoordinate.size() - 1) + " steps <br> Now you can press button \"clear\" or button \"next maze\" <html>");
                        revertPath(); // we must revert path, because by default our coordinates begin in goal position
                        printStepByStepSolution();
                        isMazeSolved = true;
                        firstAsk = false;
                    }
                } else {
                    printStepByStepSolution();
                }
            }
        });
    }

    //all variable to default status
    private void restartHelperVar() {
        mAlgorithmManager.clearResolve();
        manualClick = 0;
        firstAsk = true;
        haveEnd = false;
        haveStart = false;
        isMazeSolved = false;
        solve.setEnabled(false);
        solveStepByStep.setEnabled(false);
    }

    // reverting path
    private void revertPath() {
        List<String> revertPath = new ArrayList<>();
        for (int i = 0; i < pathCoordinate.size(); i++) {
            revertPath.add(pathCoordinate.get(pathCoordinate.size() - i - 1));
        }
        pathCoordinate = revertPath;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // then we calculate how width and height our maze are
        // and our bigger number will help to draw right size of our rectangles
        if (currentMaze.length > currentMaze[0].length) {
            dynamicDimentionHelper = currentMaze.length;
        } else {
            dynamicDimentionHelper = currentMaze[0].length;
        }


        g.translate(Utils.MARGIN_70, Utils.MARGIN_70);      //move the maze to begin at 70 from x and 70 from y

        // draw the maze
        // first of all we choose color by position
        for (int row = 0; row < currentMaze.length; row++) {
            for (int col = 0; col < currentMaze[0].length; col++) {
                Color color;
                switch (currentMaze[row][col]) {
                    case Utils.WALL:
                        color = Color.darkGray;     // block (black)
                        break;
                    case Utils.GOAL_BLOCK:
                        color = Color.GREEN;         // goal  (green)
                        break;
                    case Utils.START_BLOCK:
                        color = Color.YELLOW;      // start   (yellow)
                        break;
                    case Utils.PATH_MARKING:
                        color = Color.RED;   // the path from the start state to the goal
                        break;
                    default:
                        color = Color.WHITE;   // white free space 0  (white)
                }
                g.setColor(color);


                // we calculate right size for each square
                g.fillRect(Utils.squareAreaForRectangleSize / dynamicDimentionHelper * col, Utils.squareAreaForRectangleSize / dynamicDimentionHelper * row, Utils.squareAreaForRectangleSize / dynamicDimentionHelper, Utils.squareAreaForRectangleSize / dynamicDimentionHelper)
                ;  // fill rectangular with color
                g.setColor(Color.BLUE);                  //the border rectangle color
                g.drawRect(Utils.squareAreaForRectangleSize / dynamicDimentionHelper * col, Utils.squareAreaForRectangleSize / dynamicDimentionHelper * row, Utils.squareAreaForRectangleSize / dynamicDimentionHelper, Utils.squareAreaForRectangleSize / dynamicDimentionHelper);  //draw rectangular with color
            }

        }
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {
        private int cur_val;

        @Override
        public void mousePressed(MouseEvent evt) {

            //convert mouse click to row and col
            int row = (evt.getY() - Utils.MARGIN_70) / (Utils.squareAreaForRectangleSize / dynamicDimentionHelper);
            int col = (evt.getX() - Utils.MARGIN_70) / (Utils.squareAreaForRectangleSize / dynamicDimentionHelper);

            if (row >= 0 && row < currentMaze.length && col >= 0 && col < currentMaze[0].length) {

                //look what we have in current position
                cur_val = currentMaze[row][col];
                if (!isMazeSolved) {
                    switch (cur_val) {
                        case Utils.EMPTY_BLOCK:
                            if (!haveStart) {
                                currentMaze[row][col] = Utils.START_BLOCK;
                                haveStart = true;
                                message.setText("<html>You set start position<br> Now select end position</html>");
                                repaint();
                            } else {
                                if (!haveEnd) {
                                    currentMaze[row][col] = Utils.GOAL_BLOCK;
                                    haveEnd = true;
                                    message.setText("<html>You set end position</html>");
                                    repaint();
                                }
                            }
                            break;

                        case Utils.START_BLOCK:
                            currentMaze[row][col] = Utils.EMPTY_BLOCK;
                            haveStart = false;
                            isMazeSolved = false;
                            message.setText("You deselect start position");
                            repaint();
                            break;

                        case Utils.GOAL_BLOCK:
                            currentMaze[row][col] = Utils.EMPTY_BLOCK;
                            haveEnd = false;
                            isMazeSolved = false;
                            message.setText("You deselect end position");
                            repaint();
                            break;
                    }

                    //after all manipulation we check is it possible to find solution
                    if (haveStart && haveEnd) {
                        solve.setEnabled(true);
                        solveStepByStep.setEnabled(true);
                        message.setText("Now program can try to solve the maze");
                    } else {
                        solve.setEnabled(false);
                        solveStepByStep.setEnabled(false);
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

    }
}

