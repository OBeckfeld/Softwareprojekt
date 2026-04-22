package entities;

public class Player {
    // Die Position und Größe des Spielers
    int x, y, height, width;

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
}
