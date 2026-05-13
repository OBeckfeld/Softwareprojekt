package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;

public class Enemy extends Entity {
    Player player;//nur zum testen
    public Enemy(int x, int y, int height, int width, EntityRegistry registry, Player player, TileManager tiles) {
        super(x, y, height, width, registry, tiles);
        setSpeed(8);
        this.player = player;//nur zum testen
    }
    public void update(){
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        vector.setLength(getSpeed());
        vector.apply(this);//get getSpeed() schritte in richtung vom player
    }
}
