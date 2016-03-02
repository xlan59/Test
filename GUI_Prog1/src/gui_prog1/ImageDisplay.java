package gui_prog1;

// ImageDisplay.java
// Java Swing program to display an image in a JPanel

// import statements
import javax.swing.*;

// ImageDisplay class
public class ImageDisplay extends JPanel
{
    // constructor
    public ImageDisplay()
    {
        // add the image label to fill the panel
        JLabel image = new JLabel( new ImageIcon( "weather.jpg") );        
        this.add(image);                    
    }    
}
