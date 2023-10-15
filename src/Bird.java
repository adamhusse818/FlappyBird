/**
 * Class for implementing the behavior of the bird
 * We want to have the bird flap its wings and this class allows that
 * This class utilizes the ActionListener class
 */

// Necessary imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Bird implements ActionListener{
    Image[] wingPositions; // Storing wing positions

    Image wingDown, wingMid, wingUp, currentWingPos; // Images of each wing position, including the current one
    int yBird = 210;
    Timer timer; // Timer for how often we change the wing position
    int changeBy = 1; // Use this to change the element currently selected in the list
    int wingIndex;
    int velocity = 0;
    int acceleration = 1;
    String[] colors = {"Blue", "Yellow", "Red"};
    int colorIndex = 1;

    Bird(){
        // Initialize the list and images
        wingPositions = new Image[3];
        wingUp = new ImageIcon("Assets/Sprites/YellowBirdUpFlap.png").getImage();
        wingMid = new ImageIcon("Assets/Sprites/YellowBirdMidFlap.png").getImage();
        wingDown = new ImageIcon("Assets/Sprites/YellowBirdDownFlap.png").getImage();

        // Add the images to the list and set the initial position of the bird wings to the middle
        wingPositions[0] = wingUp;
        wingPositions[1] = wingMid;
        wingPositions[2] = wingDown;
        currentWingPos = wingPositions[1];
        wingIndex = 1;

        // Initialize the timer and start it
        timer = new Timer(85, this);
        timer.start();
    }

    public void jump(){
        if(yBird - 10 <= 0){
            yBird = 20;
        }
        velocity = -10;
        Game.playSound("Wing");
    }

    public void changeColor(){
        if(colorIndex == colors.length - 1){
            colorIndex = 0;
        }
        else{
            colorIndex++;
        }
        wingPositions[0] = new ImageIcon("Assets/Sprites/"+ colors[colorIndex] + "BirdUpFlap.png").getImage();
        wingPositions[1] = new ImageIcon("Assets/Sprites/"+ colors[colorIndex] + "BirdMidFlap.png").getImage();
        wingPositions[2] = new ImageIcon("Assets/Sprites/"+ colors[colorIndex] + "BirdDownFlap.png").getImage();
    }

    public Image getCurrentWingPos(){
        return currentWingPos;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(wingIndex ==  2 || wingIndex == 0){
            changeBy *= -1;
        }
        currentWingPos = wingPositions[wingIndex + changeBy];
        wingIndex += changeBy;
    }
}