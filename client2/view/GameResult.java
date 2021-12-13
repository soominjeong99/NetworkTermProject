package client2.view;

import processing.core.PApplet;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GameResult implements Displayable {

    private static final int TEXT_SIZE = 50;

    private final float x;
    private final float y;


    private String message;
 

    public GameResult(float x, float y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
        
    }

    @Override
    public void display(PApplet p) {
        drawText(p);
    }

    private void drawText(PApplet p) {
        p.fill(Color.BLACK.getValue());
        p.textSize(TEXT_SIZE);
        p.text(message, x, y);
    }
}