/**
 * Class for designing the interface of the panel that is added to the frame
 * The entire structure of the game is handled here
 * TO-DO: Implement file operation that saves player high score, bird color, and background
 */

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class Game extends JPanel implements ActionListener, MouseListener{
    private final String SPRITE_PATH = "Assets/Sprites/"; // Use this for all sprites
    private Image base, readyMessage, gameOverMessage, endScreen; // Images associated with the gameplay
    private int xBase; // Initial x position of the ground
    private Bird bird; // Bird object
    private Pipe[] pipes; // Array used to store the pairs of pipes
    private boolean gameStarted, gameOver; // Different states of the game
    private int score; // Score of the user
    private int yEndMessage; // Used for displaying the end message
    private int screenVelocity; // Velocity associated with the end screen
    private Button birdButton, themeButton, restartButton; // Buttons for different purposes
    private Theme background; // The background used for the game

    private ImageIcon[] numImages = {
            new ImageIcon("Assets/Sprites/0.png"),
            new ImageIcon("Assets/Sprites/1.png"),
            new ImageIcon("Assets/Sprites/2.png"),
            new ImageIcon("Assets/Sprites/3.png"),
            new ImageIcon("Assets/Sprites/4.png"),
            new ImageIcon("Assets/Sprites/5.png"),
            new ImageIcon("Assets/Sprites/6.png"),
            new ImageIcon("Assets/Sprites/7.png"),
            new ImageIcon("Assets/Sprites/8.png"),
            new ImageIcon("Assets/Sprites/9.png")
    };

    Game(){
        // Some initializations
        screenVelocity = 20;
        yEndMessage = 600;
        score = 0;
        xBase = 0;

        // Initial setting up for the panel
        this.setLayout(null);
        this.setPreferredSize(new Dimension(336, 612));
        this.addMouseListener(this);

        // Setting up the Flappy Bird environment by creating the themes, screens, and messages
        background = new Theme("Day");
        base = new ImageIcon(SPRITE_PATH + "Base.png").getImage();
        readyMessage = new ImageIcon(SPRITE_PATH + "Message.png").getImage();
        gameOverMessage = new ImageIcon(SPRITE_PATH + "GameOver.png").getImage();
        endScreen = new ImageIcon(SPRITE_PATH + "EndScreen.png").getImage();

        // Initialize the bird
        bird = new Bird("Yellow");

        // Create button for changing bird color
        birdButton = new Button("Bird Color", 25, 435);
        birdButton.addActionListener(e -> {
            if(e.getSource() == birdButton){
                bird.changeColor();
            }
        });
        this.add(birdButton);

        // Creating button to allow user to restart after dying
        restartButton = new Button("Restart", 120, 435);
        restartButton.disableButton();
        restartButton.addActionListener(e -> {
            if(e.getSource() == restartButton){
                playSound("Swoosh");
                restart();
            }
            restartButton.disableButton();
        });
        this.add(restartButton);

        // Creating button to allow user to change background
        themeButton = new Button("Theme", 200, 435);
        themeButton.addActionListener(e -> {
            if(e.getSource() == themeButton){
                background.changeTheme();
            }
        });
        this.add(themeButton);

        /*
         Initializing the array for the pipes
         The list will have 2 pipes at a time
         A new pipe is added whenever one passes the left side of the panel
         This allows for the re-generation of pipes
         */
        pipes = new Pipe[2];
        pipes[0] = new Pipe();
        pipes[1] = new Pipe();
        pipes[1].xPipe = pipes[0].xPipe + 200; // A pair of pipes will have 200 pixels of space in between the next pair

        // Initialize our states of the game
        gameStarted = false;
        gameOver = false;

        // Initialize timer
        // Used for motion of theme
        Timer timer = new Timer(30, this);
        timer.start();
    }

    /**
     * Method used for painting the entire game
     * This will determine which state the game is in, then go from there
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background.theme, 0, 0, null);
        if(!gameStarted && !gameOver){
            g2D.drawImage(readyMessage, 75, 50, null);
        }
        if(gameStarted){
            birdButton.disableButton();
            themeButton.disableButton();
            for(Pipe pipe : pipes){
                Image pImage = pipe.pipe;
                int x = pipe.xPipe;
                int y = pipe.yTopPipe;
                pipe.timer.start();
                g2D.drawImage(pImage, x, y + pImage.getHeight(null) + 100, null); // Bottom Pipe
                g2D.drawImage(pImage, x, y + pImage.getHeight(null), pImage.getWidth(null),
                        -pImage.getHeight(null), null); // Bottom pipe flipped -> top pipe
                if(x <= 48 && !pipe.passed){
                    score++;
                    pipe.passed = true;
                    playSound("Point");
                }
            }
            if(!gameOver){
                String tempScore = String.valueOf(score); // For organization purposes, score is converted to String
                for(int i = 0; i < tempScore.length(); i++){
                    String digit = tempScore.substring(i, i + 1);
                    Image digitImage = numImages[Integer.parseInt(digit)].getImage();
                    g2D.drawImage(digitImage, 152 + (20 * i), 50, null);
                }
            }
        }
       if(gameOver){
            g2D.drawImage(gameOverMessage, 75, 150, null);
            g2D.drawImage(endScreen, 20, yEndMessage, null);
            String tempScore = String.valueOf(score);
            for(int i = 0; i < String.valueOf(score).length(); i++){
                String digit = tempScore.substring(i, i + 1);
                Image digitImage = numImages[Integer.parseInt(digit)].getImage();
                g2D.drawImage(digitImage, 240 + (20 * i), yEndMessage + 45, null);
            }
            String medal = determineMedal();
            if(medal != null){
                Image medalImage = new ImageIcon(medal).getImage();
                g2D.drawImage(medalImage, 55, yEndMessage + 55, null);
            }
            restartButton.enableButton();
        }

       // Drawing what is there no matter the state of the game
        g2D.drawImage(bird.currentWingPos, bird.xBird, bird.yBird, null);
        g2D.drawImage(base, xBase, background.theme.getHeight(null), null);
        g2D.drawImage(base, xBase + base.getWidth(null), background.theme.getHeight(null), null);
    }

    /**
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(!gameOver){
            if(xBase <= -base.getWidth(null)){
                xBase = 0;
            }
            xBase -= 5;
        }
        if(gameStarted && !gameOver){
            if(pipes[0].xPipe <= -pipes[0].pipe.getWidth(null)){
                pipes[0].timer.stop();
                pipes[0] = pipes[1];
                pipes[1] = new Pipe();
            }
            bird.velocity += bird.acceleration;
            bird.yBird += bird.velocity;
            collisionTest();
        }
        if(gameOver){
            for(Pipe p : pipes){
                p.timer.stop();
            }
            bird.timer.stop();
            if(background.theme.getHeight(null) - (bird.yBird + 24) <= 1){
                bird.velocity = 0;
                bird.acceleration = 0;
            }
            bird.velocity += bird.acceleration;
            bird.yBird += bird.velocity;
            if(yEndMessage == 200){
                screenVelocity = 0;
            }
            yEndMessage -= screenVelocity;
        }
        repaint();
    }

    /**
     * This function tests if the bird collided with a pipe or the ground
     * If it does, we end the game and play the lovely sound of death
     */
    void collisionTest(){
        /*
         Since the physics don't always allow the bird to be exactly at a certain point,
         we test if the bird is within a certain amount of pixels to the area
         */
        if(background.theme.getHeight(null) - (bird.yBird + 24) <= 1){
            bird.velocity = 0;
            bird.acceleration = 0;
            gameOver = true;
            playSound("Hit");
        }
        for(Pipe p : pipes){
            if(!p.passed && ((bird.yBird <= (p.yTopPipe + 320) || bird.yBird >= (p.yTopPipe + 396))
                    && (p.xPipe >= 56 && p.xPipe <= 124))){
                gameOver = true;
                playSound("Hit");
                playSound("Die");
            }
        }
    }

    /**
     * This function restarts the game by resetting the variables and objects
     */
    void restart(){
        gameOver = false;
        gameStarted = false;
        birdButton.enableButton();
        themeButton.enableButton();
        bird = new Bird("Yellow");
        pipes[0].timer.stop();
        pipes[1].timer.stop();
        pipes[0] = new Pipe();
        pipes[1] = new Pipe();
        pipes[1].xPipe = pipes[0].xPipe + 200;
        yEndMessage = 600;
        screenVelocity = 20;
        score = 0;
    }

    /**
     * This function determines which medal the user earns
     * 10 pts is bronze, 20 pts is silver,
     * 30 pts is gold, 40 pts is platinum
     * @return a String for the medal that is earned by the user
     */
    String determineMedal(){
        String medal = SPRITE_PATH;
        if(score >= 40){
            medal += "MedalPlatinum.png";
        }
        else if(score >= 30){
            medal += "MedalGold.png";
        }
        else if(score >= 20){
            medal += "MedalSilver.png";
        }
        else if(score >= 10){
            medal += "MedalBronze.png";
        }
        else{
            return null;
        }
        return medal;
    }

    /**
     * This function plays a sound
     * If the sound does not exist, we messed up and an error is thrown
     * @param soundName a string for the sound that is played
     */
    static void playSound(String soundName){
        String audioPath = "Assets/Audio/";
        String soundFile = audioPath + soundName + ".wav";
        try{
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            clip.open(inputStream);
            clip.start();
        }catch(Exception a){
            System.out.println(a.getMessage());
            System.exit(130);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        // NOT USED
    }

    /**
     * Used to start the game and make the bird jump
     * This is only invoked if the mouse is enabled
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e){
        if(!gameOver){
            gameStarted = true;
            bird.jump();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        // NOT USED
    }

    @Override
    public void mouseEntered(MouseEvent e){
        // NOT USED
    }

    @Override
    public void mouseExited(MouseEvent e){
        // NOT USED
    }
}
