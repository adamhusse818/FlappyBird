/**
 * Class for designing the pipes
 * Each instance will generate two pipes: one that will face up and one that will face down
 * The distance between each pipe will be fair so the user will not rage-quit
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Pipe implements ActionListener{
    Image pipe;
    Timer timer;
    int xPipe; // Initial x position of every pair of pipes
    int yTopPipe;
    boolean passed;
    Pipe(){
        this.pipe = new ImageIcon("Assets/Sprites/PipeGreen.png").getImage();
        Random rand = new Random();
        this.yTopPipe = -rand.nextInt(150);
        this.xPipe = 330;
        passed = false;
        timer = new Timer(30, this);
    }

    public void actionPerformed(ActionEvent e){
        xPipe -= 5;
    }
}