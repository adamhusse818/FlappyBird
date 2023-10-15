/**
 * Class for designing the frame
 * The frame will incorporate a panel that displays game
 * A main method is included here. A new "main" file is NOT required
 * This file is used to run the game. Simply run it, and you should be able to play
 * TO-DO: Implement file operation that saves player high score, bird color, and background
 */

// Import statements
import javax.swing.JFrame;
import javax.swing.ImageIcon;

class FlappyBird extends JFrame{
    FlappyBird(){
        // Setting up the frame. Add title, change icon, set characteristics, etc.
        this.setTitle("Flappy Bird");
        
        // Use this for all file paths for sprites
        String spritePath = "Assets/Sprites/";
        this.setIconImage(new ImageIcon(spritePath + "YellowBirdMidFlap.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Initialize a new game panel
        Game panel = new Game();

        // Add the panel to the frame, pack its contents, set location on screen, then set it to be visible
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args){
        /*
         Playing audio with Java includes a slight delay for the first sound played
         To prevent this from effecting gameplay, a silent sound file is played
         The thread is set to sleep for half a second to prevent the initial freeze
         */
        Game.playSound("Silent");
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        // Game begins here :)
        new FlappyBird();
    }
}
