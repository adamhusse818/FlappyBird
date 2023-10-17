/**
 * Class used for making the theme of the background
 */

// Import statements
import javax.swing.ImageIcon;
import java.awt.Image;

public class Theme{
    Image theme; // Used to store image of background;
    private int themeIndex; // Keep track of the index of the current theme
    private String[] themes = {"Day", "Night"}; // Array of the backgrounds available
    private final String SPRITE_PATH = "Assets/Sprites/Background"; // Use this for all file paths for sprites

    public Theme(String theme){
        this.theme = new ImageIcon(SPRITE_PATH + theme + ".png").getImage();
        this.themeIndex = 0;
    }

    public void changeTheme(){
        if(this.themeIndex == themes.length - 1){
            this.themeIndex = 0;
            this.theme = new ImageIcon(SPRITE_PATH + themes[0] + ".png").getImage();
        }
        else{
            this.theme = new ImageIcon(SPRITE_PATH + themes[themeIndex + 1] + ".png").getImage();
            this.themeIndex++;
        }
    }
}
