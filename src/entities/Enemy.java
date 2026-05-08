package entities;

import entities.managers.EntityRegistry;
import tools.Vector;

public class Enemy extends PlayerTypeEntity {
    Player player;//nur zum testen
    public Enemy(int x, int y, int height, int width, EntityRegistry registry, Player player) {
        super(x, y, height, width, 360, registry);
        setSpeed(8);
        this.player = player;//nur zum testen
        this.verticalRange = 120; // größe des Rectangles von Attack bei einem Enemy
        this.horizontalRange = 60;
    }
    public void update(){
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        vector.setLength(getSpeed());
        vector.apply(this);//get getSpeed() schritte in richtung vom player
    }
}
