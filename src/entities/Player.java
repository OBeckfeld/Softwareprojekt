package entities;

import java.awt.event.KeyEvent;
import java.util.Set;

public class Player {
    // Die Position und Größe des Spielers
    int x, y, height, width;
    int speed = 5; // Pixel, mit denen sich der Spieler pro Frame bewegt

    public Player(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Methoden, um die Position zu verändern (wird vom KeyboardInput aufgerufen)
    public void changeX(int amount) {
        x += amount;
    }

    public void changeY(int amount) {
        y += amount;
    }

    // Getter-Methoden, damit das GamePanel weiß, wo es das Rechteck zeichnen muss
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void update(Set<Integer> keyboardInputs) {
        int dx = 0, dy = 0;
        if(keyboardInputs.contains(KeyEvent.VK_W)){ dy -= speed; }
        if(keyboardInputs.contains(KeyEvent.VK_S)){ dy += speed; }
        if(keyboardInputs.contains(KeyEvent.VK_A)){ dx -= speed; }
        if(keyboardInputs.contains(KeyEvent.VK_D)){ dx += speed; }

        x += dx;
        y += dy;
    }
}
