package UPEProject;

// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    // Welcome Screen junk
    int fontSize = 75; 
    int minFontSize = 75; 
    int maxFontSize = 100; 
    int fontSizeChange = 1; 

    // Background proportions/frame measurements
    public static final int SCREEN_WIDTH = 1300;
    public static final int SCREEN_HEIGHT = 750;
    public static final int UNIT_SIZE = 50;
    public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private BufferedImage gridImage;

    // Player stats regarding Snake
    final int x1[] = new int[GAME_UNITS];
    final int y1[] = new int[GAME_UNITS];
    final int x2[] = new int[GAME_UNITS];
    final int y2[] = new int[GAME_UNITS];
    int bodyParts1 = 5;
    int bodyParts2 = 5;
    int moves = 0;
    char direction1 = 'U'; 
    char direction2 = 'D';
    
    // Player stats regarding Stocks
    int balance1 = 1000;
    int balance2 = 1000;
    int shares1 = 0;
    int shares2 = 0;
    int purchases = 0;
    int sales = 0;
    int networth1;
    int networth2;

    // Stock price determinants
    int[] stockHistory = new int[1000];
    int stockPrice = 500;
    int changedPrice = 0;
    int luck;
    String[] currentHeadlines = new String[3];

    // Apple states
    Random random;
    private int appleX;
    private int appleY;

    // Turn determinants
    int currentPlayer = 1;
    int turn = 0;
    boolean newTurn = true;

    // Game booleans for constantly changing variables
    boolean stockChange = false;
    boolean appleOnScreen = false; 
    boolean appleEaten = true;

    // Game over state booleans and values
    boolean gameOver = false;
    boolean richSnake = false;
    boolean running = false; // Game running state
    int loser;

    // Lists (Store repetetive code/text elements)
    private List<JButton> gameButtons = new ArrayList<>();
    String newsList[] = {   "<html>Possible Shortages In Apples To Cause Surge In Demand And Price</html>",
                            "<html>AppleCorp Announces Lower Than Expected Earnings Return</html>",
                            "<html>Snakify Releases New Track 'Snake Me To Church', Receiving Neutral Reviews</html>",
                            "<html>'Upsssilon Python Epsssilon' To Announce Deliberations Soon, Causing Global Apprehension</html>",
                            "<html>Apple Suppliers Issue Recall In A Dozen Regions Due To Venomous Allegations</html>",
                            "<html>Tom Shanks To Star In 'Snatch Me If You Can', A Movie On Apple Fraud</html>",
                            "<html>Snake Oil Market Booms: Sly Investors Reap Rewards From Malpractices</html>",
                            "<html>Snake Collision On The SnakeWay Causes Fear Around Ssself-Driving Technology</html>",
                            "<html>[Insert Celebrity Nonsense Here (No Effect On The Global Market)]</html>",
                            "<html>Programmers Rejoice Worldwide Over New Python Update, Boosting Downloads</html>",
                            "<html>PyGame Library Plundered By The Viper Gang, Causing Games Worldwide To Break</html>",
                            "<html>What's Your SSSodiac Sign? Here Are What SerpentGazers Say</html>",
                            "<html>GroundSnakes Arise Early, Causing Early Blooms And Spring Sales</html>",
                            "<html>Oversaturation In Python Market Leads To Lower Wages, According To Hisstorians</html>",
                            "<html>Local Snake Charmer Competition Leads To The Two Winners Marrying Each Other</html>"
                            };
    
    // Constructor for the GamePanel
    public GamePanel() {
        random = new Random();
        
        createGridImage();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.requestFocusInWindow();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Timer needed for welcome screen animations, changing font size and stock graph
        if(running == false){
            Timer textTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(running == false){
                    fontSize += fontSizeChange;
                    if (fontSize > maxFontSize || fontSize < minFontSize) {
                        fontSizeChange *= -1;
                    }
                    repaint();
                }
            }
        });
        textTimer.start();
        }  
    }
    // Create the game grids, and store as images to prevent infinite looping
    public void createGridImage() {
        gridImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = gridImage.createGraphics();
        // Background color = black
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2d.setColor(Color.white);
        // Vertical lines
        for (int i = 0; i * UNIT_SIZE < SCREEN_WIDTH; i++) {
            if (i < 15) {
                g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            } 
            else if(i == 15){
                g2d.setColor(Color.yellow);
                g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g2d.setColor(Color.white);
            }
            else {
                g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, 400);
            }
        }
        // Horizontal lines
        for (int i = 0; i * UNIT_SIZE < SCREEN_HEIGHT; i++) {
            if(i <= 8){
                g2d.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            else{
                g2d.drawLine(0, i * UNIT_SIZE, 750, i * UNIT_SIZE);
            }
        }
        // Remove unnecessary lines
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, UNIT_SIZE, 15 * UNIT_SIZE);  // Left side
        g2d.fillRect(0, 0, 15 * UNIT_SIZE, UNIT_SIZE);  // Top side
        g2d.fillRect(0, 14 * UNIT_SIZE, 15 * UNIT_SIZE, UNIT_SIZE);  // Bottom side
        g2d.fillRect(14 * UNIT_SIZE, 0, UNIT_SIZE, 15 * UNIT_SIZE);  // Right side
        g2d.setColor(Color.white);
        g2d.drawLine(UNIT_SIZE, 14 * UNIT_SIZE, 14 * UNIT_SIZE, 14 * UNIT_SIZE);
        g2d.drawLine(14 * UNIT_SIZE, UNIT_SIZE, 14 * UNIT_SIZE, 14 * UNIT_SIZE);
        g2d.dispose();
    }
    
    // Repaint method to update screen after each turn or button click
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Game state: If running, do the following:
        if (running == true && gameOver == false) {
            // Redraw the grid
            g.drawImage(gridImage, 0, 0, this);
            // Redraw the buttons, find new news
            newsHeadlines();
            // If there's no apple then get new apple coordinates
            if (appleEaten) {
                newApple(); 
                appleEaten = false; 
            }
            // If the stock price changed then redraw the chart
            if(stockChange == true){
                updateStockPrice();
                drawChart();
                stockChange = false;

            }
            // Change the scoreboard's colors depending on the turn
            if(currentPlayer == 1){
                g.setColor(Color.green);
            }
            else{
                g.setColor(Color.red);
            }
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            String turnLabel = "Current Turn: Player " + currentPlayer;
            g.drawString(turnLabel, 875, 450);
            // Redraw the buttons by removing and adding them again. Prevents the loop into oblivion situation
            removeButtons();
            drawButtons(g);
            // Draw the apple (If the coordinates didn't change then it redraws on the same coordinates as last time.)
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE - 1, UNIT_SIZE - 1);
            // Draw the players for good measure
            drawPlayers(g);
        }
        // Game state: If the game isn't over but isn't running, show the start screen
        else if(running == false && gameOver == false){
            drawStartScreen(g);
            // Get ready for the game to start
            initializePlayers();
        }
        // Game state: If the game is over
        else{
            // No need for the buttons anymore, get rid of them :(
            removeButtons();
            // Game substate: If the game ended on player 1's turn:
            if(currentPlayer == 1){
                // Default game over screen by losing at Snake
                g.setColor(Color.red);
                g.setFont(new Font("Ink Free", Font.BOLD, 150));
                String gameOver1 = "GAME OVER";
                g.drawString(gameOver1, 225, 300);
                g.setFont(new Font("Ink Free", Font.BOLD, 100));
                String winMessage1 = "Bested by Player 2";
                // If player 1 ends their turn with less than half of player 2's net worth:
                if(richSnake == true){
                    // Change the default game over message to reflect bankruptcy, readjust coordinates
                    g.setFont(new Font("Ink Free", Font.BOLD, 50));
                    winMessage1 = "Player " + loser + " Has Insufficient Funds :(";
                    g.drawString(winMessage1, 200, 450);
                }
                else{
                    g.drawString(winMessage1, 250, 450);
                }
            }
            // Game substate: If the game ended on player 2's turn:
            else{
                // Default game over screen by losing at Snake
                g.setColor(Color.green);
                g.setFont(new Font("Ink Free", Font.BOLD, 150));
                String gameOver2 = "GAME OVER";
                g.drawString(gameOver2, 225, 300);
                g.setFont(new Font("Ink Free", Font.BOLD, 100));
                String winMessage2 = "Bested by Player 1";
                // If player 2 ends their turn with less than half of player 1's net worth: 
                if(richSnake == true){
                    // Change the default game over message to reflect bankruptcy, readjust coordinates
                    g.setFont(new Font("Ink Free", Font.BOLD, 50));
                    winMessage2 = "Player " + loser + " Has Insufficient Funds :(";
                    g.drawString(winMessage2, 200, 450);
                }
                else{
                    g.drawString(winMessage2, 250, 450);
                }
            }
        }
    }

    // Generate coordinates for a new apple on an empty tile if there is no apple on the screen
    public void newApple(){
        if (!appleOnScreen) {
            boolean appleOnSnake;
            do {
                appleOnSnake = false;
                appleX = UNIT_SIZE * (int)(Math.random() * 13) + UNIT_SIZE;
                appleY = UNIT_SIZE * (int)(Math.random() * 13) + UNIT_SIZE;
                // Check if the apple is on the snake's body
                for (int i = 0; i < bodyParts1; i++) {
                    if (appleX == x1[i] && appleY == y1[i]) {
                        appleOnSnake = true;
                        break;
                    }
                }
                if (!appleOnSnake) {
                    for (int i = 0; i < bodyParts2; i++) {
                        if (appleX == x2[i] && appleY == y2[i]) {
                            appleOnSnake = true;
                            break;
                        }
                    }
                }
            } while (appleOnSnake);
            appleOnScreen = true; 
        }  
    }
    
    // Initialize player coordinates, net worth calculation, starting stock price
    public void initializePlayers() {
        x1[0] = 650; 
        y1[0] = 650; 
        x2[0] = 50;
        y2[0] = 50;
        networth1 = balance1 + ((stockPrice * shares1) * 9)/10;
        networth2 = balance2 + ((stockPrice * shares2) * 9)/10;
        stockHistory[0] = 500;
    }

    // Title Screen (Clicking the space bar will exit the title screen)
    public void drawStartScreen(Graphics g){
        // Drawing the shrinking and growing start button + title screen
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 150));
        String text = "Snake";
        String text2 = "Brokers";
        g.drawString(text, 200, 275);
        g.setFont(new Font("Times New Roman", Font.BOLD, 150));
        g.setColor(Color.red);
        g.drawString(text2, 600, 275);
        String start = "Press SPACE to start";
        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free", Font.BOLD, fontSize));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        int x1 = (getWidth() - metrics1.stringWidth(start)) / 2;
        int y1 = (getHeight() / 2);
        // Drawing the graph lines
        g.drawString(start, x1, y1);
        int tempheight = SCREEN_HEIGHT;
        int tempheight1 = 0;
        for (int i = 0; i * UNIT_SIZE < SCREEN_WIDTH; i++){
            int randomvar = (int)(Math.random() * 50);
            int newheight = tempheight - randomvar;
            int newheight1 = tempheight1 + randomvar;
            g.setColor(Color.green);
            g.drawLine(i * UNIT_SIZE, tempheight, (i + 1) * UNIT_SIZE, newheight);
            g.setColor(Color.red);
            g.drawLine(i * UNIT_SIZE, tempheight1, (i + 1) * UNIT_SIZE, newheight1);
            tempheight = newheight;
            tempheight1 = newheight1;
        }
    }
    // Generate 3 news headlines for the iPhone feature. Indices 0, 3, 6... boost stock profit (From the luck variable) while indices 1, 4, 7... lower them.
    // Feel free to add your own headlines using this rule!! 
    public void newsHeadlines() {
        if (newTurn) {
            int headline1;
            int headline2;
            int headline3;
            headline1 = (int) (Math.random() * newsList.length);
            headline2 = (int) (Math.random() * newsList.length);
            headline3 = (int) (Math.random() * newsList.length);
            if (headline1 % 3 == 0) {
                luck++;
            } 
            else if (headline1 % 3 == 1) {
                luck--;
            }
            while (headline1 == headline2) {
                headline2 = (int) (Math.random() * newsList.length);
                if (headline2 != headline1) {
                    if (headline2 % 3 == 0) {
                        luck++;
                    } 
                    else if (headline1 % 3 == 1) {
                        luck--;
                    }
                    break;
                }
            }
            while (true) {
                headline3 = (int) (Math.random() * newsList.length);
                if (headline3 != headline1 && headline3 != headline2) {
                    if (headline3 % 3 == 0) {
                        luck++;
                    } 
                    else if (headline1 % 3 == 1) {
                        luck--;
                    }
                    break;
                }
            }
            currentHeadlines[0] = newsList[headline1];
            currentHeadlines[1] = newsList[headline2];
            currentHeadlines[2] = newsList[headline3];
            newTurn = false;
        }
    }
    
    // Move feature depending on direction. After each move, check if apple is eaten and if a collision is made, then redraw
    public void move(int[] x, int[] y, char direction, int bodyParts) {
        if(moves < bodyParts / 2){
            moves++;
            for (int i = bodyParts - 1; i >= 1; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
            switch (direction) {
                case 'L':
                    x[0] -= UNIT_SIZE;
                    // Debugging statement
                    // System.out.println("Moved snake " + currentPlayer + " to " + x[0] + " " + y[0]); 
                    checkApple(x, y);
                    checkCollisions(x, y, bodyParts);
                    repaint();
                    break;
                case 'U':
                    y[0] -= UNIT_SIZE;
                    checkApple(x, y);
                    checkCollisions(x, y, bodyParts);
                    repaint();
                    break;
                case 'D':
                    y[0] += UNIT_SIZE;
                    checkApple(x, y);
                    checkCollisions(x, y, bodyParts);
                    repaint();
                    break;
                case 'R':
                    x[0] += UNIT_SIZE;
                    checkApple(x, y);
                    checkCollisions(x, y, bodyParts);
                    repaint();
                    break;
            }
        }
    }
    
    // Check if the snake head is on the same coordinates as the apple, readjust boolean values to reflect this for the next screen repaint, and get new apple coordinates
    public void checkApple(int[] x, int[] y) {
        if (x[0] == appleX && y[0] == appleY){
            appleEaten = true; 
            appleOnScreen = false; 
            newApple(); 
            if(currentPlayer == 1){
                bodyParts1++;
                balance1 += 100;
                x1[bodyParts1 - 1] = x1[bodyParts1 - 2];
                y1[bodyParts1 - 1] = y1[bodyParts1 - 2];
            }
            else{
                bodyParts2++;
                balance2 += 100;
                x2[bodyParts2 - 1] = x2[bodyParts2 - 2];
                y2[bodyParts2 - 1] = y2[bodyParts2 - 2];
            }
            drawPlayers(getGraphics());
        }
    }
    
    // Make sure that players have relatively enough money to continue playing. If not, activate the special win condition boolean richSnake
    public void checkStocks(int networth1, int networth2){
        if(networth1 >= (networth2 * 2)){
            running = false;
            gameOver = true;
            richSnake = true;
            loser = 2;
            return;
        }
        else if(networth2 >= (networth1 * 2)){
            running = false;
            gameOver = true;
            richSnake = true;
            loser = 1;
            return;
        }
    }

    // Make sure that players don't collide with each other or their own bodies
    public void checkCollisions(int[] x, int[] y, int bodyParts) {
        // Check if the current player's snake collides with itself
        for (int i = 1; i < bodyParts; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                gameOver = true;
                return;
            }
        }
        // Check if the current player's snake collides with the other player's snake
        int[] otherX;
        int[] otherY;
        int otherBodyParts;
        if (currentPlayer == 1) {
            otherX = x2;
            otherY = y2;
            otherBodyParts = bodyParts2;
        } 
        else {
            otherX = x1;
            otherY = y1;
            otherBodyParts = bodyParts1;
        }
        for (int i = 0; i < otherBodyParts; i++) {
            if (x[0] == otherX[i] && y[0] == otherY[i]) {
                running = false;
                gameOver = true;
                return;
            }
        }
        if (x[0] < UNIT_SIZE || x[0] >= UNIT_SIZE * 14 || y[0] < UNIT_SIZE || y[0] >= UNIT_SIZE * 14) {
            running = false;
            gameOver = true;
            return;
        }
    }
    
    // Draws all game buttons and stores their functions
    public void drawButtons(Graphics g) {
        // Move buttons: Set a direction depending on player, and call the move method
        JButton moveUp = new JButton("ʌ");
        moveUp.setFont(new Font("Serif Bold", Font.BOLD, 25));
        moveUp.setBounds(350, 0, 50, 50);
        moveUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer == 1 && direction1 != 'D') {
                    direction1 = 'U';
                    move(x1, y1, direction1, bodyParts1); 
                } 
                else if (currentPlayer == 2 && direction2 != 'D') {
                    direction2 = 'U';
                    move(x2, y2, direction2, bodyParts2); 
                }
            }
        });

        JButton moveDown = new JButton("v");
        moveDown.setFont(new Font("Serif Bold", Font.BOLD, 25));
        moveDown.setBounds(350, 700, 50, 50);
        moveDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer == 1 && direction1 != 'U') {
                    direction1 = 'D';
                    move(x1, y1, direction1, bodyParts1); 
                } 
                else if (currentPlayer == 2 && direction2 != 'U') {
                    direction2 = 'D';
                    move(x2, y2, direction2, bodyParts2); 
                }
            }
        });

        JButton moveLeft = new JButton("<");
        moveLeft.setFont(new Font("Serif Bold", Font.BOLD, 25));
        moveLeft.setBounds(0, 350, 50, 50);
        moveLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer == 1 && direction1 != 'R') {
                    direction1 = 'L';
                    move(x1, y1, direction1, bodyParts1);
                } 
                else if (currentPlayer == 2 && direction2 != 'R') {
                    direction2 = 'L';
                    move(x2, y2, direction2, bodyParts2); 
                }
            }
        });

        JButton moveRight = new JButton(">");
        moveRight.setFont(new Font("Serif Bold", Font.BOLD, 25));
        moveRight.setBounds(700, 350, 50, 50);
        moveRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer == 1 && direction1 != 'L') {
                    direction1 = 'R';
                    move(x1, y1, direction1, bodyParts1); 
                } 
                else if (currentPlayer == 2 && direction2 != 'L') {
                    direction2 = 'R';
                    move(x2, y2, direction2, bodyParts2); 
                }
            }
        });

        // Buy stock only if current player hasn't bought once yet. Repaint to reflect changes
        JButton buyStock = new JButton("Buy ($" + stockPrice + ")");
        buyStock.setBackground(Color.green);
        buyStock.setFont(new Font("Serif Bold", Font.BOLD, 25));
        buyStock.setBounds(775, 625, 250, 50); 
        buyStock.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                if(purchases == 0){
                    if(currentPlayer == 1){
                        balance1 -= stockPrice;
                        shares1++;
                    }
                    else{
                        balance2 -= stockPrice;
                        shares2++;
                    }
                    purchases++;
                    checkStocks(networth1, networth2);
                    repaint();
                }
            }
        });

        // Sell stock only if current player hasn't sold yet. Repaint to reflect changes
        JButton sellStock = new JButton("Sell ($" + stockPrice + ")");
        sellStock.setBackground(Color.red);
        sellStock.setFont(new Font("Serif Bold", Font.BOLD, 25));
        sellStock.setBounds(1025, 625, 250, 50);
        sellStock.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(sales == 0){
                    if(currentPlayer == 1){
                        if(shares1 > 0){
                            balance1 += stockPrice;
                            shares1--;
                        }
                    }
                    else{
                        if(shares2 > 0){
                            balance2 += stockPrice;
                            shares2--;
                        }
                    }
                    sales++;
                    checkStocks(networth1, networth2);
                    repaint();
                }
            }
        });

        // Display player balances, share value, and net worths
        int networth1 = balance1 + ((stockPrice * shares1) * 9)/10;
        int networth2 = balance2 + ((stockPrice * shares2) * 9)/10;
        
        // Calculate the required net worth difference
        int requiredDifference1 = (2 * networth2) - networth1;
        int requiredDifference2 = (2 * networth1) - networth2;

        String cash1 = "<html>Green's Bank: $" + balance1 + " | Shares: " + shares1 + " ($" + shares1 * stockPrice + ")<br>Net Worth: $" + networth1 + " | +$" + requiredDifference1 + " to win</html>";
        String cash2 = "<html>Red's Bank: $" + balance2 + " | Shares: " + shares2 + " ($" + shares2 * stockPrice + ")<br>Net Worth: $" + networth2 + " | +$" + requiredDifference2 + " to win</html>";

        JButton player1 = new JButton(cash1);
        player1.setBounds(775, 475, 500, 75);
        player1.setFont(new Font("Serif Bold", Font.BOLD, 20));

        JButton player2 = new JButton(cash2);
        player2.setBounds(775, 550, 500, 75);
        player2.setFont(new Font("Serif Bold", Font.BOLD, 20));
       
        // Display news button, generating 3 headlines every turn that affect stock performance. Opens a new window in the shape of an iPhone
        JButton news = new JButton("News");
        news.setFont(new Font("Serif Bold", Font.BOLD, 25));
        news.setBounds(775, 675, 250, 50);
        news.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openNewWindow();
            }
        });

        // End turn based on conditions: Player must have moved at least one tile. Number of sales, purchases, and booleans are changed to reflect the end and beginning of a turn.
        JButton endTurn = new JButton("End turn");
        endTurn.setFont(new Font("Serif Bold", Font.BOLD, 25));
        endTurn.setBounds(1025, 675, 250, 50);
        endTurn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(moves > 0){
                    checkStocks(networth1, networth2);
                    turn++;
                    if (currentPlayer == 1) {
                        currentPlayer = 2;
                    } 
                    else {
                        currentPlayer = 1;
                    }             
                    stockChange = true; 
                    newTurn = true; 
                    moves = 0;
                    sales = 0;
                    purchases = 0;
                    luck = 0;
                    repaint();
                }
            }
        });

        // Adding each button to the screen, and then each to the array list for removal use later
        add(moveUp);
        add(moveDown);
        add(moveLeft);
        add(moveRight);
        add(buyStock);
        add(sellStock);
        add(news);
        add(endTurn);
        add(player1);
        add(player2);
        gameButtons.add(moveUp);
        gameButtons.add(moveDown);
        gameButtons.add(moveLeft);
        gameButtons.add(moveRight);
        gameButtons.add(buyStock);
        gameButtons.add(sellStock);
        gameButtons.add(news);
        gameButtons.add(endTurn);
        gameButtons.add(player1);
        gameButtons.add(player2);
    }
    
    // Remove all buttons based on an array list. This removes the buttons spawning into oblivion issue
    public void removeButtons() {
        for (JButton button : gameButtons) {
            remove(button);
        }
        gameButtons.clear(); 
    }
    
    // Opens an iPhone shaped tab with the news headlines when player clicks the news button. Also shows the "time" and a poorly done wallpaper just for jokes!
    private void openNewWindow() {
        JFrame newFrame = new JFrame("PRETEND THIS IS AN IPHONE");
        newFrame.setSize(400, 820);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);
        newFrame.setResizable(false);
        JPanel iPhoneScreen = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (running == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.fillRect(0, 0, 400, 820);
                    g2d.setColor(Color.white); // Example color
                    g2d.fillRect(30, 60, 325, 665);
                    g2d.setColor(Color.gray);
                    g2d.fillOval(170, 728, 50, 50);
                    g2d.fillOval(135, 28, 10, 10);
                    g2d.fillRect(160, 28, 80, 10);
                    g2d.fillOval(155, 28, 10, 10);
                    g2d.fillOval(235, 28, 10, 10);
                    g2d.setColor(Color.black);
                    g2d.fillRect(180, 738, 30, 30);
                    g2d.setColor(Color.green);
                    g2d.setStroke(new BasicStroke(3));
                    int[] xPoints = {60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340};
                    int[] yPoints = {350, 250, 230, 243, 214, 190, 202, 123, 123, 80, 97, 81, 69, 69};
                    int nPoints = 14;
                    g2d.drawPolyline(xPoints, yPoints, nPoints);
                    g.setFont(new Font("Monotype Corsiva", Font.BOLD, 69));
                    g2d.setColor(Color.black);
                    String turnText = "Turn " + turn;
                    g2d.drawString(turnText, 105, 150);
                }
            }
        };

        // Let iPhone be movable, and set the text for each button
        iPhoneScreen.setLayout(null);
        JButton news1 = new JButton(currentHeadlines[0]);
        JButton news2 = new JButton(currentHeadlines[1]);
        JButton news3 = new JButton(currentHeadlines[2]);
        news1.setBounds(50, 370, 285, 100);
        news2.setBounds(50, 485, 285, 100);
        news3.setBounds(50, 600, 285, 100);

        // Add the buttons to the iPhoneScreen panel
        iPhoneScreen.add(news1);
        iPhoneScreen.add(news2);
        iPhoneScreen.add(news3);
    
        // Add the iPhoneScreen panel to the newFrame
        newFrame.add(iPhoneScreen);
        newFrame.setVisible(true);
    }
    
    // Draw the chart with the stock history. After 10 turns, the chart rescales itself to fit inside the window rather than running off screen.
    public void drawChart(){
        int scale = 500 / turn;
        Graphics2D g3 = gridImage.createGraphics();
        g3.setColor(Color.yellow);
        int offsetX = 750;
        int offsetY = -700;
        boolean rect = false;
        g3.setStroke(new BasicStroke(3));

        for(int i = 1; i <= turn; i++){
                if(stockHistory[i] == 0){
                    break;
                }
                if(turn < 11){
                    int stockX = offsetX + (i - 1) * UNIT_SIZE;
                    int stockX2 = ((i * UNIT_SIZE) + offsetX);
                    int stockY = -(stockHistory[i - 1] + offsetY);
                    int stockY2 = -(stockHistory[i] + offsetY);
                    g3.drawLine(stockX, stockY, stockX2, stockY2);
                }
                else{
                    if(turn >= 11 && rect == false){    
                        g3.setStroke(new BasicStroke(1));
                        g3.setColor(Color.black);
                        g3.fillRect(751, 0, 1350, 399);
                        g3.setColor(Color.white);
                        for (int x = 16; x * UNIT_SIZE < SCREEN_WIDTH; x++) {
                            g3.drawLine(x * UNIT_SIZE, 0, x * UNIT_SIZE, 400);
                        }
                        for (int y = 0; y < 8; y++) {
                            g3.drawLine(750, y * UNIT_SIZE, SCREEN_WIDTH, y * UNIT_SIZE);
                        }
                        g3.setColor(Color.yellow);
                        rect = true;
                        g3.setStroke(new BasicStroke(3));
                    }
                int stockX = offsetX + (i - 1) * scale;
                int stockX2 = (((i) * scale) + offsetX);
                int stockY = -(stockHistory[i - 1] + offsetY);
                int stockY2 = -(stockHistory[i] + offsetY);
                g3.drawLine(stockX, stockY, stockX2, stockY2);
            }
        }
    }
    
    // Depending on luck from news headlines and random number generators, make the stock price go up or down, and store the value in the graph
    public void updateStockPrice() {
        // System.out.println("turn " + turn); (Debugging)
        // Generate a random number between 10 and -10, then add 5 times the luck value
        int priceChange = ((int) (Math.random() * 21) - 10) - (luck * 15);
        // Get the most recent stock price, recalculate new price and add it to stock history
        int recentPrice = stockHistory[turn - 1];
        int newPrice = recentPrice + priceChange;   
        stockHistory[turn] = newPrice;
        stockPrice = newPrice;
        // System.out.println("New price is " + newPrice); (Debugging)        
        drawChart();
    }
    
    // Draw or redraw each player
    public void drawPlayers(Graphics g) {
        // Draw player 1 (green)
        g.setColor(Color.green);
        for (int i = 0; i < bodyParts1; i++) {
            if (i == 0) {
                // Light green
                g.setColor(new Color(0, 128, 0)); 
            } 
            else {
                // Dark green 
                g.setColor(new Color(0, 100, 0)); 
            }
            g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
        }
    
        // Draw player 2 (red)
        g.setColor(Color.red);
        for (int i = 0; i < bodyParts2; i++) {
            if (i == 0) {
                // Light red
                g.setColor(new Color(255, 100, 100));
            } 
            else {
                // Dark red
                g.setColor(new Color(139, 0, 0));
            }
            g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
        }
    }
    
    // Detects when the space bar is clicked, and when it is, change the game state to running 
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (!running) {
                if (key == KeyEvent.VK_SPACE) {
                    running = true;
                    repaint(); 
                }
            } 
        }
    }
    
    // I'm not using this but my code doesn't run without it, it feels like the appendix of the project 
    public void actionPerformed(ActionEvent e) {
    }
}
