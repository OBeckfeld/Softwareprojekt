package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.Vector;

import java.util.ArrayList;

public class Enemy extends Entity {
    Player player;//nur zum testen

    public Enemy(int x, int y, int height, int width, EntityRegistry registry, Player player) {
        super(x, y, height, width, registry);
        setSpeed(3);
        this.player = player;//nur zum testen
    }
    public void update(){
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView){
            if (entity instanceof Player){
                Vector vector = new Vector(getX(), getY(), entity.getX(), entity.getY());
                if (vector.getLength() > getSpeed()){ //damit er nicht immer über sein ziel rübergeht
                    vector.setLength(getSpeed());
                }

                if (registry.getInRange(this, 100).contains(entity)){
                    vector.setLength(0);
                }
                if (registry.collidesWith(this, entity)){
                    vector= new Vector(entity.getCenter() [0], entity.getCenter() [1], getCenter() [0], getCenter() [1]);
                    vector.setLength(5);
                }
                vector.apply(this);//getSpeed() schritte in richtung vom player

            }

        }
    }
}
