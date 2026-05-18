package entities.components;

import entities.Entity;

import java.awt.event.KeyEvent;
import java.util.Set;

import tools.TileManager;

public class MovementComponent {

    private TileManager tileManager;
    public MovementComponent(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public void move(Set<Integer> keyboardInputs, Entity player) {

        double dx = 0, dy = 0; //Veränderung der X und Y Koordinaten genau
        int vx, vy; //Veränderung der X und Y Koordinaten auf int gerundet
        int tileSize = 64;

        //prüfen, welche Inputs gegeben werden
        if (keyboardInputs.contains(KeyEvent.VK_W)) {
            dy -= player.getSpeed();
        }
        if (keyboardInputs.contains(KeyEvent.VK_S)) {
            dy += player.getSpeed();
        }
        if (keyboardInputs.contains(KeyEvent.VK_A)) {
            dx -= player.getSpeed();
        }
        if (keyboardInputs.contains(KeyEvent.VK_D)) {
            dx += player.getSpeed();
        }

        //für diagonale Bewegung
        if (dx != 0 && dy != 0) {
            vx = (int) Math.round(dx * 0.70710678118);
            vy = (int) Math.round(dy * 0.70710678118);
        } else {
            vx = (int) Math.round(dx);
            vy = (int) Math.round(dy);
        }
        int playerWidth  = player.getWidth();
        int playerHeight = player.getHeight();
        double newX = player.getX() + vx;
        double newY = player.getY() + vy;
        if(vx>0){
            // rechts prüfen
            double right = newX + playerWidth -1;
            double tileCol   = right / tileSize;
            double tileRowTop    = player.getY() / tileSize;
            double tileRowBottom = (player.getY() + playerHeight - 1) / tileSize;
            if (isWall((int)tileRowTop, (int)tileCol) ||
                    isWall((int)tileRowBottom, (int)tileCol)) {
                newX = tileCol * tileSize - 2.3 - playerWidth;
            }
        }
        else if (vx < 0) {
            // links prüfen
            double tileCol       = newX / tileSize;
            double tileRowTop    = player.getY() / tileSize;
            double tileRowBottom = (player.getY() + playerHeight - 1) / tileSize;

            if (isWall((int)tileRowTop, (int)tileCol) ||
                    isWall((int)tileRowBottom, (int)tileCol)) {
                newX = tileCol * tileSize + 2.3;
            }
        }


        if (vy > 0) {
            // unten prüfen
            double bottomEdge = newY + playerHeight - 1;
            double tileRow    = bottomEdge / tileSize;
            double tileColLeft  = player.getX() / tileSize;
            double tileColRight = (player.getX() + playerWidth - 1) / tileSize;

            if (isWall((int)tileRow, (int)tileColLeft) ||
                    isWall((int)tileRow, (int)tileColRight)) {
                newY = tileRow * tileSize - playerHeight - 2.3;
            }
        } else if (vy < 0) {
            // oben prüfen
            double tileRow      = newY / tileSize;
            double tileColLeft  = player.getX() / tileSize;
            double tileColRight = (player.getX() + playerWidth - 1) / tileSize;

            if (isWall((int)tileRow, (int)tileColLeft) ||
                    isWall((int)tileRow, (int)tileColRight)) {
                newY = tileRow * tileSize + 2.3;
            }
        }



        player.setX(newX);

        player.setY(newY);


    }

    private boolean isWall(int row, int col) {
        int[][] map = tileManager.getCurrentMap().getLayout();

        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return true;
        }

        return map[row][col] != 8;
    }
}
