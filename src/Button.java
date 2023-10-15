import javax.swing.*;

public class Button extends JButton{
    Button(String text, int x, int y){
        this.setFocusable(false);
        this.setLayout(null);
        this.setText(text);
        this.setBounds(x, y, 100, 50);
    }

    public void enableButton(){
        this.setVisible(true);
        this.setEnabled(true);
    }

    public void disableButton(){
        this.setVisible(false);
        this.setEnabled(false);
    }
}
