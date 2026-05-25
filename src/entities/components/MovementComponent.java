package entities.components;

import entities.Entity;
import entities.PlayerTypeEntity;
import inputs.KeyboardInputs;
import tools.Vector;

import java.awt.event.KeyEvent;
import java.util.Set;

import tools.TileManager;

public class MovementComponent {

    private KeyboardInputs inputs;
    private TileManager tileManager;

    public MovementComponent(){}
    public MovementComponent(KeyboardInputs inputs, TileManager tileManager) {
        this.tileManager = tileManager;
        this.inputs = inputs;
    }

    public void move(PlayerTypeEntity entity) {
        if (inputs == null) {
            return;
        }

        double dx = 0, dy = 0;
        int tileSize = tileManager.getTileSize();

        if(inputs.getHeldKeys().contains(KeyEvent.VK_W)){
            dy -= entity.getSpeed();
            (entity).setDirection(3);
        }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_S)){
            dy +=  entity.getSpeed();
            (entity).setDirection(1);
        }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_A)){
            dx -=  entity.getSpeed();
            ((PlayerTypeEntity)entity).setDirection(2);
        }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_D)){
            dx += entity.getSpeed();
            (entity).setDirection(0);
        }

        //für diagonale Bewegung
        if(dx != 0 && dy != 0){
            dx = dx * 0.70710678118;
            dy = dy * 0.70710678118;
        }

        entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);

        entity.setX(entity.getX() + dx);
        entity.setY(entity.getY() + dy);

        //geht durch Wände durch, wenn oben und unten keine Wand ist
        // rechts prüfen
        int playerWidth  = entity.getWidth();
        int playerHeight = entity.getHeight();
        double newX = entity.getX() + dx;
        double newY = entity.getY() + dy;
        if(dx>0){
            double right = newX + playerWidth;
            double tileCol   = right / tileSize;
            double tileRowTop    = entity.getY() / tileSize;
            double tileRowBottom = (entity.getY() + playerHeight) / tileSize;
            if (isWall((int)tileRowTop, (int)tileCol) ||
                    isWall((int)tileRowBottom, (int)tileCol)) {
                newX = tileCol * tileSize - playerWidth - 2;
            }
        }
        else if (dx < 0) {
            // links prüfen
            double tileCol       = newX / tileSize;
            double tileRowTop    = entity.getY() / tileSize;
            double tileRowBottom = (entity.getY() + playerHeight - 1) / tileSize;

            if (isWall((int)tileRowTop, (int)tileCol) ||
                    isWall((int)tileRowBottom, (int)tileCol)) {
                newX = tileCol * tileSize + 2;
            }
        }


        if (vy > 0) {
            // unten prüfen
            double bottomEdge = newY + playerHeight;
            double tileRow    = bottomEdge / tileSize;
            double tileColLeft  = entity.getX() / tileSize;
            double tileColRight = (entity.getX() + playerWidth - 1) / tileSize;

            if (isWall((int)tileRow, (int)tileColLeft) ||
                    isWall((int)tileRow, (int)tileColRight)) {
                newY = tileRow * tileSize - playerHeight - 2;
            }
        } else if (vy < 0) {
            // oben prüfen
            double tileRow      = newY / tileSize;
            double tileColLeft  = entity.getX() / tileSize;
            double tileColRight = (entity.getX() + playerWidth - 1) / tileSize;

            if (isWall((int)tileRow, (int)tileColLeft) ||
                    isWall((int)tileRow, (int)tileColRight)) {
                newY = tileRow * tileSize + 2;
            }
        }



        player.setX(newX);

        player.setY(newY);
}

    public void move(Entity entity, double dx, double dy) {

        entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);

        entity.setX(entity.getX() + dx);
        entity.setY(entity.getY() + dy);

    }
    public void applyVector(Entity entity, Vector vector) {
        move(entity, vector.getOffsetX(), vector.getOffsetY());
        }
    private boolean isWall(int row, int col) {
        int[][] map = tileManager.getCurrentMap().getLayout();

        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return true;
        }

        return map[row][col] != 8;
    }
}
