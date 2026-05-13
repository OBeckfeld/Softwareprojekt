package entities.components;

import entities.Entity;
import inputs.KeyboardInputs;
import tools.TileManager;

import java.awt.event.KeyEvent;

public class MovementComponent {

    private KeyboardInputs inputs;
    private TileManager tiles;

    public MovementComponent(){}
    public MovementComponent(KeyboardInputs inputs, TileManager tiles){
        this.inputs = inputs;
        this.tiles = tiles;

    }
    private TileManager map;
//player muss noch zu entity geändert werden
    public void move(Entity entity) {
        map = tiles;
        double dx = 0, dy = 0; //Veränderung der X und Y Koordinaten genau
        int vx, vy ; //Veränderung der X und Y Koordinaten auf int gerundet
        double curX = entity.getX();
        double curY = entity.getY();
        //prüfen, welche Inputs gegeben werden
        if(inputs.getHeldKeys().contains(KeyEvent.VK_W)){ dy -= entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_S)){ dy += entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_A)){ dx -= entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_D)){ dx += entity.getSpeed(); }

        //für diagonale Bewegung
        if(dx != 0 && dy != 0){
            dx = dx * 0.70710678118;
            dy = dy * 0.70710678118;
        }
        //double a = tiles.getCurrentMap()[(curX+dx)/7.14][(curY+dy)/7.14];
        //if(tiles.getCurrentMap()[(curX+dx)/7.14][(curY+dy)/7.14]==1||2||3||4||5||6||7)
        //{

        //}
        //else{
            entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);
            entity.setX(entity.getX() + dx);
            entity.setY(entity.getY() + dy);
        //}

    }

    public void move(Entity entity, double dx, double dy) {

        entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);

        entity.setX(entity.getX() + dx);

        entity.setY(entity.getY() + dy);

    }
}
