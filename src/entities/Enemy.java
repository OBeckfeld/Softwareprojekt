package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.Vector;

import java.util.ArrayList;

public class Enemy extends Entity {
    public Enemy(int x, int y, int height, int width, EntityRegistry registry) {
        super(x, y, height, width, registry);
        setSpeed(3);
        mass = 1;
        viewRange = 500;
    }
    public void update(){
        super.update();
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView) {


            if (entity instanceof Player) {//player im sichtfeld
                Vector vector = new Vector(getX(), getY(), entity.getX(), entity.getY());

                if (vector.getLength() > getSpeed()) { //damit er nicht immer über sein ziel rübergeht
                    vector.setLength(getSpeed());
                }

                if (registry.getInRange(this, 100).contains(entity)) { //gegner stoppen wenn sie angreifen können
                    vector.setLength(0);
                }
                applyVector(vector);//schritt ausführen
            }
        }
    }
}
