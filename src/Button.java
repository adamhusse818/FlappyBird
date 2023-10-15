/**
 * Class used for designing the buttons
 * The buttons will be used to let the user change the bird color, background, or restart
 */

// Only need JButton here
import javax.swing.JButton;

public class Button extends JButton{
    Button(String text, int x, int y){
        // Initial setups for each button
        this.setFocusable(false);
        this.setLayout(null);
        this.setText(text);
        this.setBounds(x, y, 100, 50); // The buttons will be the same size
    }

    /**
     * Method used for enabling the button
     * Helpful when switching between being at the menu or during the game
     */
    public void enableButton(){
        this.setVisible(true);
        this.setEnabled(true);
    }

    /**
     * Method used for disabling the button
     * Helpful when switching between being at the menu or during the game
     */
    public void disableButton(){
        this.setVisible(false);
        this.setEnabled(false);
    }
}
