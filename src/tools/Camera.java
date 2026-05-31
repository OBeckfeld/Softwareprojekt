package tools;

import entities.Player;
import main.Game;

public class Camera {
    //top left coordinates
    private double x, y;

    //camera dimensions
    private final int viewWidth;
    private final int viewHeight;

    //gameWorld boundaries
    private int worldWidth = Game.getWIDTH();
    private int worldHeight = Game.getHEIGHT();

    public Camera(double startX, double startY, int screenWidth, int screenHeight) {
        this.x = startX;
        this.y = startY;
        viewWidth = screenWidth;
        viewHeight = screenHeight;
    }

    public void update(Player player) {
        worldWidth = Game.getWIDTH();
        worldHeight = Game.getHEIGHT();
        //center of player - half view width = new x position of camera, same for y
        double targetX = (player.getX() + (double) player.getWidth() / 2) - (double) viewWidth / 2;
        double targetY = (player.getY() + (double) player.getHeight() / 2) - (double) viewHeight / 2;

        //wird 10% in Richtung des Spielers pro Tick bewegt
        x += (targetX - x) * 0.1f;
        y += (targetY - y) * 0.1f;

        //damit die Camera nicht out of bounds schauen kann
        if(x < 0) x = 0;
        if(y < 0) y = 0;
        if(x > worldWidth - viewWidth) x = worldWidth - viewWidth;
        if(y > worldHeight - viewHeight) y = worldHeight - viewHeight;
    }

    public double getX() {return x;}
    public void setX(double x) {this.x = x;}
    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
}
