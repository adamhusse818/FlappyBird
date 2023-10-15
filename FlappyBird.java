/**
 * Class for designing the frame
 * The frame will display the game and allow the user to play
 * This class extends the javax.swing.JFrame class
 */

// Import statement
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class FlappyBird extends JFrame{
    ImageIcon frameIcon = new ImageIcon("Assets/Sprites/YellowBirdMidFlap.png");
    Game panel;
    FlappyBird(){
        // Setting up the frame. Add title, change icon, set characteristics, etc.
        this.setTitle("Flappy Bird");
        this.setIconImage(frameIcon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Initialize the game panel
        panel = new Game();

        // Add the panel to the frame, set some more characteristics, then set it to be visible
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args){
        Game.playSound("Silent");
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        new FlappyBird();
    }
}