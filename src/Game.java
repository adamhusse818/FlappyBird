/**
 * Class for designing the interface for actually playing
 * It will use a new panel to display the game
 * This class extends the javax.swing.JPanel class
 */

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Game extends JPanel implements ActionListener, MouseListener{
    Image background, base, readyMessage, gameOverMessage, endScreen; // Images associated with the gameplay
    int xBase = 0; // Initial x position of the ground
    Timer timer; // Used for motion of background
    Bird bird; // Bird object
    Pipe[] pipes;
    boolean gameStarted;
    boolean gameOver;
    boolean mouseEnabled = true;
    int score = 0;
    int y = 600;
    int screenVelocity = 20;
    Button birdButton, themeButton, restartButton;

    ImageIcon[] nums = {
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
        // Initial setting up for the panel
        this.setLayout(null);
        this.setPreferredSize(new Dimension(336, 612));
        this.addMouseListener(this);

        // Setting up the Flappy Bird environment by creating the background and message
        background = new ImageIcon("Assets/Sprites/BackgroundNight.png").getImage();
        base = new ImageIcon("Assets/Sprites/Base.png").getImage();
        readyMessage = new ImageIcon("Assets/Sprites/Message.png").getImage();

        gameOverMessage = new ImageIcon("Assets/Sprites/GameOver.png").getImage();
        endScreen = new ImageIcon("Assets/Sprites/EndScreen.png").getImage();

        // Initialize the bird
        bird = new Bird();

        birdButton = new Button("Bird Color", 25, 435);
        birdButton.addActionListener(e -> {
            if(e.getSource() == birdButton){
                bird.changeColor();
            }
        });

        restartButton = new Button("Restart", 120, 435);
        restartButton.disableButton();
        this.add(restartButton);
        restartButton.addActionListener(e -> {
            if(e.getSource() == restartButton){
                playSound("Swoosh");
                restart();
            }
            restartButton.disableButton();
        });

        this.add(birdButton);

        themeButton = new Button("Theme", 200, 435);

        this.add(themeButton);

        pipes = new Pipe[2];
        pipes[0] = new Pipe();
        pipes[1] = new Pipe();
        pipes[1].xPipe = pipes[0].xPipe + 200;

        gameStarted = false;
        gameOver = false;

        // Initialize timers
        timer = new Timer(30, this);
        timer.start();
    }

    /**
     *
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background, 0, 0, null);
        if(gameStarted){
            birdButton.disableButton();
            themeButton.disableButton();
            for(Pipe pipe : pipes){
                Image p = pipe.pipe;
                int x = pipe.xPipe;
                int y = pipe.yTopPipe;
                pipe.timer.start();
                g2D.drawImage(p, x, y + p.getHeight(null) + 100, null);
                g2D.drawImage(p, x, y + p.getHeight(null), p.getWidth(null), -p.getHeight(null), null);
                if(x <= 48 && !pipe.passed){
                    score++;
                    pipe.passed = true;
                    playSound("Point");
                }
            }
            if(!gameOver){
                int tempScore = score;
                for(int i = 0; i < String.valueOf(score).length(); i++){
                    int digit = tempScore % 10;
                    Image digitImage = nums[digit].getImage();
                    g2D.drawImage(digitImage, 152 - (20 * i), 50, null);
                    tempScore /= 10;
                }
            }
        }
        else if(!gameOver){
            g2D.drawImage(readyMessage, 75, 50, null);
        }
        if(gameOver){
            g2D.drawImage(gameOverMessage, 75, 150, null);
            g2D.drawImage(endScreen, 20, y, null);
            int tempScore = score;
            for(int i = 0; i < String.valueOf(score).length(); i++){
                int digit = tempScore % 10;
                Image digitImage = nums[digit].getImage();
                g2D.drawImage(digitImage, 240 - (20 * i), y + 45, null);
                tempScore /= 10;
            }
            String medal = determineMedal();
            if(medal != null){
                Image medalImage = new ImageIcon(medal).getImage();
                g2D.drawImage(medalImage, 55, y + 55, null);
            }
            restartButton.enableButton();
        }
        g2D.drawImage(bird.getCurrentWingPos(), 90, bird.yBird, null);
        g2D.drawImage(base, xBase, background.getHeight(null), null);
        g2D.drawImage(base, xBase + base.getWidth(null), background.getHeight(null), null);
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
            if(gameStarted){
                if(pipes[0].xPipe <= -pipes[0].pipe.getWidth(null)){
                    pipes[0].timer.stop();
                    pipes[0] = pipes[1];
                    pipes[1] = new Pipe();
                }
                bird.velocity += bird.acceleration;
                bird.yBird += bird.velocity;
                collisionTest();
            }
        }
        else{
            for(Pipe p : pipes){
                p.timer.stop();
            }
            bird.timer.stop();
            if(background.getHeight(null) - (bird.yBird + 24) <= 1){
                bird.velocity = 0;
                bird.acceleration = 0;
            }
            bird.velocity += bird.acceleration;
            bird.yBird += bird.velocity;
            if(y == 200){
                screenVelocity = 0;
            }
            y -= screenVelocity;
        }
        repaint();
    }

    public void collisionTest(){
        if(background.getHeight(null) - (bird.yBird + 24) <= 1){
            bird.velocity = 0;
            bird.acceleration = 0;
            mouseEnabled = false;
            gameOver = true;
            playSound("Hit");
        }
        for(Pipe p : pipes){
            if(!p.passed && ((bird.yBird <= (p.yTopPipe + 320) || bird.yBird >= (p.yTopPipe + 396))
                    && (p.xPipe >= 56 && p.xPipe <= 124))){
                mouseEnabled = false;
                gameOver = true;
                playSound("Hit");
                playSound("Die");
            }
        }
    }

    public void restart(){
        gameOver = false;
        gameStarted = false;
        mouseEnabled = true;
        birdButton.enableButton();
        themeButton.enableButton();
        bird = new Bird();
        pipes[0].timer.stop();
        pipes[1].timer.stop();
        pipes[0] = new Pipe();
        pipes[1] = new Pipe();
        pipes[1].xPipe = pipes[0].xPipe + 200;
        y = 600;
        screenVelocity = 20;
        score = 0;
    }

    public String determineMedal(){
        String medal = "Assets/Sprites/";
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

    public static void playSound(String soundName){
        String path = "Assets/Audio/";
        String extension = ".wav";
        String soundFile = path + soundName + extension;
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
    }

    @Override
    public void mousePressed(MouseEvent e){
        if(mouseEnabled){
            gameStarted = true;
            bird.jump();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseExited(MouseEvent e){
    }
}