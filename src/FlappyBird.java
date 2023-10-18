/**
 * Class for designing the frame
 * The frame will incorporate a panel that displays game
 * A main method is included here. A new "main" file is NOT required
 * This file is used to run the game. Simply run it, and you should be able to play
 */

// Import statements
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;

public class FlappyBird extends JFrame implements WindowListener{
    public FlappyBird(){
        // Setting up the frame. Add title, change icon, set characteristics, etc.
        this.setTitle("Flappy Bird");

        // Use this for all file paths for sprites
        String spritePath = "assets/sprites/";
        this.setIconImage(new ImageIcon(spritePath + "YellowBirdMidFlap.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Initialize a new game panel
        Game panel = new Game();

        // Add the panel to the frame, pack its contents, set location on screen, then set it to be visible
        this.add(panel);
        this.addWindowListener(this);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e){
        // NOT USED
    }

    /**
     * Function used to determine if the window closes
     * We use this to write the user data
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e){
        try{
            FileWriter fw = new FileWriter("data.txt");
            String toWrite = "";
            for(int i = 0; i < 3; i++){
                if(i == 2){
                    toWrite += Game.data[i];
                }
                else{
                    toWrite += Game.data[i] + ",";
                }
            }
            fw.write(toWrite);
            fw.close();
        }catch(IOException a){
            System.out.println(a.getMessage());
        }
    }

    @Override
    public void windowClosed(WindowEvent e){
        // NOT USED
    }

    @Override
    public void windowIconified(WindowEvent e){
        // NOT USED
    }

    @Override
    public void windowDeiconified(WindowEvent e){
        // NOT USED
    }

    @Override
    public void windowActivated(WindowEvent e){
        // NOT USED
    }

    @Override
    public void windowDeactivated(WindowEvent e){
        // NOT USED
    }

    public static void main(String[] args){
        /*
         Playing audio with Java includes a slight delay for the first sound played
         To prevent this from effecting gameplay, a silent sound file is played
         The thread is set to sleep for half a second to prevent the initial freeze
         */
        Game.playSound("silent");
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

        // Game begins here :)
        new FlappyBird();
    }
}
