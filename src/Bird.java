/**
 * Class for implementing the behavior of the bird
 */

// Necessary imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Bird implements ActionListener{
    final String spritePath = "Assets/Sprites/"; // Use this for all file paths for sprites
    private final Image[] wingPositions; // Storing wing positions
    Image wingDown, wingMid, wingUp, currentWingPos; // Images of each wing position, including the current one
    int wingIndex; // Keep track of the index of the current wing position
    final int xBird = 90; // X position of the bird. This MUST remain unchanged
    int yBird = 210; // Initial y position of the bird
    Timer timer; // Timer for how often we change the wing position
    int changeBy = 1; // Use this to change the element currently selected in the list
    int velocity = 0; // Setting velocity of the bird
    int acceleration = 1; // Setting acceleration of the bird
    final String[] colors = {"Blue", "Yellow", "Red"}; // These are the different colors the bird can be
    int colorIndex = 1; // Keep track of the color index. Original color is yellow, hence it being 1

    Bird(String color){
        // Initialize the list and images for each bird wing position
        wingPositions = new Image[3];
        wingUp = new ImageIcon(spritePath + color + "BirdUpFlap.png").getImage();
        wingMid = new ImageIcon(spritePath + color + "BirdMidFlap.png").getImage();
        wingDown = new ImageIcon(spritePath + color + "BirdDownFlap.png").getImage();

        // Add the images to the list and set the initial position of the bird wing to the middle
        wingPositions[0] = wingUp;
        wingPositions[1] = wingMid;
        wingPositions[2] = wingDown;
        currentWingPos = wingPositions[1];
        wingIndex = 1;

        // Initialize the timer and start it
        timer = new Timer(85, this);
        timer.start();
    }

    /**
     * Method used to make the bird jump
     * The bird will simply have its velocity negative to have it go up the panel
     * A sound is also played to give it the nice effect
     * The method also tests to make sure the bird stays within the panel
     */
    public void jump(){
        if(yBird - 10 <= 0){
            yBird = 20;
        }
        velocity = -10;
        Game.playSound("Wing");
    }

    /**
     * Method used to change the color of the bird
     */
    public void changeColor(){
        // Check to make sure we aren't at the end of the list
        if(colorIndex == colors.length - 1){
            colorIndex = 0;
        }
        else{
            colorIndex++; // Change color index
        }

        // Use index to change the wing positions to match the bird color
        wingPositions[0] = new ImageIcon(spritePath + colors[colorIndex] + "BirdUpFlap.png").getImage();
        wingPositions[1] = new ImageIcon(spritePath + colors[colorIndex] + "BirdMidFlap.png").getImage();
        wingPositions[2] = new ImageIcon(spritePath + colors[colorIndex] + "BirdDownFlap.png").getImage();
    }

    /**
     * This method is used to give the bird a flapping effect
     * It is invoked every 85 milliseconds because of the timer
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        /*
         Check to make sure we are not at the end of the list
         If we are, we have to go backwards
         */
        if(wingIndex ==  2 || wingIndex == 0){
            changeBy *= -1;
        }

        // After determining whether we should go forwards or backwards, we change the wing position
        currentWingPos = wingPositions[wingIndex + changeBy];
        wingIndex += changeBy;
    }
}
