package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

public interface Designs {
    Rectangle gameArea = new Rectangle(0, 0, 460, 520);
    Dimension gameInfoArea = new Dimension(450, 50);
    Dimension gamePlayArea = new Dimension(450, 450);

    Font infoFont = new Font("Sanserif", Font.BOLD, 13);
    Font game1ButtonFont = new Font("Sanserif", Font.BOLD, 10);
    Font game2ButtonFont = new Font("Sanserif", Font.BOLD, 30);
    Font game3CountFont = new Font("Sanserif", Font.BOLD, 30);
    
    Color color0 = new Color( 34,  51,  79); // navy
    Color color1 = new Color(105, 124, 160); // steel blue
    Color color2 = new Color(135, 195, 233); // light blue
    Color color3 = new Color(140, 151,  77); // grass
    Color color4 = new Color(185,  79, 113); // wine
    Color color5 = new Color(220, 220, 213); // apricot
}
