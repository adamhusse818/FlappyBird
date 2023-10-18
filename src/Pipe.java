/**
 * Class for designing the pipes and their behaviour
 * The class will generate one pipe
 * The second one is made by flipping the pipe
 */

// Import statements
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Pipe implements ActionListener{
    Image pipe; // Image of the pipe
    Timer timer; // Timer used for moving the pipe
    int xPipe; // Initial x position of every pair of pipes
    int yTopPipe; // Y position of the top pipe. Used for separation
    boolean passed; // Keep track of whether the bird has passed the pair of pipes
    public Pipe(){
        // Use this for all file paths for sprites
        String spritePath = "assets/sprites/";
        this.pipe = new ImageIcon(spritePath + "PipeGreen.png").getImage();

        // Use the random class to generate a random y value for the pipe
        Random rand = new Random();
        this.yTopPipe = -rand.nextInt(150);

        // All the pipes will start at the same x value
        this.xPipe = 330;
        this.passed = false;
        this.timer = new Timer(30, this);
    }

    /**
     * Method used to move the pair of pipes to the left
     * This method is invoked every 30 milliseconds due to the timer
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e){
        this.xPipe -= 5;
    }
}
