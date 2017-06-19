import model.ReadMazeFromDisc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Traverser extends JFrame {
    public static int startY;
    public static int startX;
    public static int goalX;
    public static int goalY;
    static final char emptyBlock = ' ';
    final static char pathMarking = '0';
    final String filePath = "";

    JButton solveStepByStep;
    JButton solve;
    JButton clear;
    JButton exit;
    JButton nextMaze;
    JLabel message;  // message to the user

    List<char[][]> mazes;
    ReadMazeFromDisc readMazeFromDisc;

    static char[][] currentMaze;
    char[][] currentMazeforRestart;

    private int squareSize = 400;
    boolean repaint = false;

    // the path
    final static int V = ' ';
    //assign numbers for every color that will be used, colors are defined later (line:349 in paint)
    //block (black square)
    final static char X = 'X';


//    public static char[][] charArray;

    //List with path coordinates
    public static List<String> path = new ArrayList<String>();

    //Text colors
    public static final String AINSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    private int mSuperNumber;

    private int manualclick;
    private boolean firstask = true;
    private boolean haveStart = false;
    private boolean haveEnd = false;


    public static void main(String[] args) {

        new Traverser();              // we create new class which will invoke the constructor

    }

    Traverser() {
        readMazeFromDisc = new ReadMazeFromDisc();
        mazes = readMazeFromDisc.getMazes();


        char[][] maze = mazes.get(0);
        printBoard(maze);
        currentMaze = maze;
        path = new ArrayList<String>();
        saveMazeForRestart(maze);

        setViews();
        solve.setEnabled(false);
        solveStepByStep.setEnabled(false);

        message.setText("<html> Hello. This is maze solver.<br> Please select start and end position by clicking to the empty box in maze </html>");


//        new Traverser();
    }

    private void saveMazeForRestart(char[][] maze) {
        currentMazeforRestart = new char[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                currentMazeforRestart[i][j] = maze[i][j];
            }
        }
    }

//    Traverser() {
//        //Read maze text files
//        File folder = new File("./src/Maze");
//        File[] files = folder.listFiles();
//        for(File file : files) {
//            String fileName = file.getName();
//            if(fileName.contains(".txt")) {
//                path = new ArrayList<String>();
//                charArray = buildArrayFromTextFile(file);
//                System.out.println(fileName + ":");
//                printBoard();
//
//                //Find solution
//                Node solution = getSolution();
//
//                if(solution == null) {
//                    System.out.println("No solution");
//                }
//                else {
//                    System.out.println("Solution: ");
//                    solution.getSolutionCoords();
//                    printSolution();
//                    //System.out.println(solution.toString());
//                }
//            }
//        }
//    }

    void printBoard(char[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            System.out.println();
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j]);
            }
        }
        System.out.println("\n");
    }


    //    //Print path in red
    void printSolution() {
        for (int i = 0; i < currentMaze.length; i++) {
            System.out.println();
            for (int j = 0; j < currentMaze[0].length; j++) {
                String currentCoords = i + "," + j;
                if (path.contains(currentCoords)) {
                    System.out.print(ANSI_RED + pathMarking + AINSI_BLACK);

                    currentMaze[i][j] = '0';

                } else {
                    System.out.print(currentMaze[i][j]);
                }
            }
        }
        currentMaze[startY][startX] = 'S';
        currentMaze[goalY][goalX] = 'G';
        System.out.println("\n");
        repaint();


    }

    private void printStepByStepSolution() {
        if (manualclick <= path.size() - 1) {
            String s = path.get(manualclick);
            String[] split = s.split(",");
            currentMaze[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = '0';
            manualclick++;
            repaint();
            if (!(path.size() == manualclick)) {
                message.setText("<html>You want to solve the maze step-by-step.<br> You have to make " + (path.size() - manualclick) + " steps</html>");
            } else {
                message.setText("<html>Congrats! Maze was solved after " + path.size() + "steps </html>");

            }
        }


    }

//    //Convert text file to 2D char array
//    char[][] buildArrayFromTextFile (File _mazeFile) {
//
//        List<String> lines = new ArrayList<String>();
//
//        try {
//            Scanner sc = new Scanner(_mazeFile);
//            while (sc.hasNextLine()) {
//                String line = sc.nextLine();
//                //store this line to string [] here
//                lines.add(line);
//            }
//        }
//        catch (FileNotFoundException e) {
//            System.out.println("Error: Maze file not found");
//        }
//
//        //Calculate maze width and height
//        mazeWidth = lines.get(0).length();
//        mazeHeight = lines.size();
//
//        //Convert to array
//        char[][] array = new char[mazeHeight][mazeWidth];
//
//        for(int i = 0; i < mazeHeight; i ++) {
//            array[i] = lines.get(i).toCharArray();
//
//            //Find start and goal coordinates
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
//        }
//
//        return array;
//    }

    public Node getSolution() {
        Queue<Node> list = new LinkedList<Node>();
        Queue<String> exploredCoords = new LinkedList<String>();

        //Make an initial parent node of our first possible move
        getPosition(currentMaze);

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
                if (currentMaze[i][j] == ReadMazeFromDisc.goalBlock) {
                    goalY = i;
                    goalX = j;
                } else if (currentMaze[i][j] == ReadMazeFromDisc.startBlock) {
                    startY = i;
                    startX = j;
                }
            }
        }
    }


    void setViews() {
        setTitle("Maze");     //Title For JFrame
        setSize(960, 530);    // Size For JFrame  (width,height)


//        URL urlIcon = getClass().getResource("flat-theme-action-maze-icon.png");    // Path for image for The JFrame
//        ImageIcon image = new ImageIcon(urlIcon);                                   // store the image in variable named image
//        setIconImage(image.getImage());                                             // set The Image For JFrame

        setLocationRelativeTo(null);                                                // to make JFrame appear in the Middle of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             // To close the app when click on exit or (X)
        setLayout(null);                                                            // to set the position of the components (like JLabel and JTextField, Buttons) and by hand (we will choose the position by ourselves)

//        elapsedDfs = new JLabel("Elapsed Time :");
//        elapsedBFS = new JLabel("Elapsed Time :");
//        textDfs = new JTextField();
//        textBFS = new JTextField();

        // initialize objects for Buttons
        solveStepByStep = new JButton("Step-by-step");
        solve = new JButton("Solve");
        clear = new JButton("Clear");
        exit = new JButton("Exit");
        nextMaze = new JButton("Next Maze");

        message = new JLabel("boom", JLabel.CENTER);
        message.setForeground(Color.blue);
        message.setFont(new Font("Helvetica", Font.PLAIN, 16));

        // Add The Buttons to JFrame
        add(solveStepByStep);
        add(solve);
        add(clear);
//        add(elapsedDfs);
//        add(textDfs);
//        add(elapsedBFS);
//        add(textBFS);
        add(exit);
        add(nextMaze);
        add(message);

        setVisible(true);

        // set the positions of the components on the JFrame (x,y,width,height). here we chose the position by hand, this is why we sat the set Layout to null
        message.setBounds(500, 180, 400, 80);
        solveStepByStep.setBounds(500, 50, 100, 40);
        solve.setBounds(630, 50, 100, 40);
        clear.setBounds(760, 50, 100, 40);
        exit.setBounds(760, 115, 100, 40);
//        elapsedDfs.setBounds(500, 100, 100, 40);
        nextMaze.setBounds(500, 115, 230, 40);
//        elapsedBFS.setBounds(630, 100, 100, 40);
//        textDfs.setBounds(500, 130, 100, 25);
//        textBFS.setBounds(630, 130, 100, 25);

        MouseHandler listener = new MouseHandler();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        solve.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Node solution = getSolution();

                if (solution == null) {
                    System.out.println("No solution");
                    message.setText("Sorry, Maze don`t have solution");
                } else {
                    System.out.println("Solution: ");
                    solution.getSolutionCoords();
                    printSolution();
                    System.out.println(solution.toString());
                    message.setText("<html>Maze was solved, after " + (Traverser.path.size() - 1) + " steps <br> Now you can press button \"clear\" or button \"next maze\" <html>");
                    solve.setEnabled(false);
                    solveStepByStep.setEnabled(false);
                }

            }
        });

        nextMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSavedMaze();
                mazes.add(mazes.get(0));
                mazes.remove(0);
                currentMaze = mazes.get(0);
                saveMazeForRestart(currentMaze);
                path = new ArrayList<>();
                manualclick = 0;
                firstask = true;
                haveEnd = false;
                haveStart = false;
                startX = ' ';
                startY = ' ';
                goalX = ' ';
                goalY = ' ';
                solve.setEnabled(false);
                solveStepByStep.setEnabled(false);
                repaint();
                message.setText("<html>We show new maze <br> Please select start and end position </html>");
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = new ArrayList<>();
                manualclick = 0;
                firstask = true;
                haveEnd = false;
                haveStart = false;
                startX = ' ';
                startY = ' ';
                goalX = ' ';
                goalY = ' ';
                getSavedMaze();
                solve.setEnabled(false);
                solveStepByStep.setEnabled(false);
                repaint();
                message.setText("<html>You refrash a maze<br> Please select start and end position </html>");
            }
        });

        // what happen when click on Exit Button
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);        //Close The App
            }
        });

        solveStepByStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstask) {
                    Node solution = getSolution();


                    if (solution == null) {
                        System.out.println("No solution");
                        message.setText("Sorry, but maze don`t have solution");
                    } else {
                        System.out.println("Solution: ");
                        solution.getSolutionCoords();
                        revertPath();
                        printStepByStepSolution();
                        firstask = false;
                    }

                } else {
                    printStepByStepSolution();
                }
            }
        });
    }


    private void revertPath() {

        List<String> revertPath = new ArrayList<>();
        for (int i = 0; i < Traverser.path.size(); i++) {
            revertPath.add(Traverser.path.get(Traverser.path.size() - i - 1));
        }
        Traverser.path = revertPath;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(70, 70);      //move the maze to begin at 70 from x and 70 from y

        // draw the maze
        if (repaint == true) {  // what to do if the repaint was set to true (draw the maze as a problem without the solution)
            for (int row = 0; row < currentMaze.length; row++) {
                for (int col = 0; col < currentMaze[0].length; col++) {
                    Color color;
                    switch (currentMaze[row][col]) {
                        case 'X':
                            color = Color.darkGray;     // block (black)
                            break;
                        case 'G':
                            color = Color.GREEN;         // goal  (red)
                            break;
                        case 'S':
                            color = Color.YELLOW;      //initial state   (yellow)
                            break;
                        case '0':
                            color = Color.RED;   // the path from the initial state to the goal
                            break;
                        default:
                            color = Color.WHITE;   // white free space 0  (white)
                    }
                    g.setColor(color);

                    repaint(g, row, col);

                }
            }
        }

        if (repaint == false) {   // what to do if the repaint was set to false (draw the solution for the maze)
            for (int row = 0; row < currentMaze.length; row++) {
                for (int col = 0; col < currentMaze[0].length; col++) {
                    Color color;
                    switch (currentMaze[row][col]) {
                        case 'X':
                            color = Color.darkGray;     // block (black)
                            break;
                        case 'G':
                            color = Color.GREEN;         // goal  (red)
                            break;
                        case 'S':
                            color = Color.YELLOW;      //initial state   (yellow)
                            break;
                        case '0':
                            color = Color.RED;   // the path from the initial state to the goal
                            break;
                        default:
                            color = Color.WHITE;   // white free space 0  (white)
                    }
                    g.setColor(color);

                    repaint(g, row, col);

                }

            }

        }
    }

    private void repaint(Graphics g, int row, int col) {
        if (currentMaze.length > currentMaze[0].length) {
            mSuperNumber = currentMaze.length;
        } else {
            mSuperNumber = currentMaze[0].length;
        }
        g.fillRect(400 / mSuperNumber * col, 400 / mSuperNumber * row, 400 / mSuperNumber, 400 / mSuperNumber)
        ;  // fill rectangular with color
        g.setColor(Color.BLUE);                  //the border rectangle color
        g.drawRect(400 / mSuperNumber * col, 400 / mSuperNumber * row, 400 / mSuperNumber, 400 / mSuperNumber);  //draw rectangular with color
    }

    public void getSavedMaze() {
        for (int i = 0; i < currentMaze.length; i++) {
            for (int j = 0; j < currentMaze[0].length; j++) {
                currentMaze[i][j] = currentMazeforRestart[i][j];
            }
        }
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {
        private int cur_row, cur_col, cur_val;

        @Override
        public void mouseClicked(MouseEvent evt) {
            int row = (evt.getY() - 70) / (squareSize / mSuperNumber);
            int col = (evt.getX() - 70) / (squareSize / mSuperNumber);

            System.out.println(col);
            System.out.println(evt.getX());
            System.out.println(row);
            System.out.println(evt.getY());
            if (row >= 0 && row < currentMaze.length && col >= 0 && col < currentMaze[0].length) {

                cur_row = row;
                cur_col = col;
                cur_val = currentMaze[row][col];
                if (cur_val == V) {
                    if (!haveStart) {
                        currentMaze[row][col] = 'S';
                        startX = col;
                        startY = row;
                        haveStart = true;
                        message.setText("<html>You set start position<br> Now select end position</html>");
                    } else {
                        if (!haveEnd) {
                            currentMaze[row][col] = 'G';
                            goalX = col;
                            goalY = row;
                            haveEnd = true;
                            message.setText("<html>You set end position</html>");
                        }
                    }
                } else if (cur_val == 'S') {

                    currentMaze[row][col] = ' ';
                    startX = ' ';
                    startY = ' ';
                    haveStart = false;
                    message.setText("You deselect start position");
                } else if (cur_val == 'G') {
                    currentMaze[row][col] = ' ';
                    goalX = ' ';
                    goalY = ' ';
                    haveEnd = false;
                    message.setText("You deselect end position");
                }

                if (haveStart && haveEnd) {
                    solve.setEnabled(true);
                    solveStepByStep.setEnabled(true);
                    message.setText("Now program can try to solve the maze");
                } else {
                    solve.setEnabled(false);
                    solveStepByStep.setEnabled(false);

                }

                Runnable runnable = () -> repaint();
                runnable.run();


            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

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

