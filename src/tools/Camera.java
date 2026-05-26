package tools;

import entities.Player;
import main.Game;

public class Camera {
    //top left coordinates
    private double x, y;

    //camera dimensions
    private final int viewWidth = 1920;
    private final int viewHeight = 1080;

    //gameWorld boundaries
    private final int worldWidth = Game.getWIDTH();
    private final int worldHeight = Game.getHEIGHT();

    public Camera(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }

    public void update(Player player) {
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
    public double getY() {return y;}
}
