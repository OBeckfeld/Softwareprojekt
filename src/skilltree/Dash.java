package skilltree;

import entities.PlayerTypeEntity;
import entities.components.MovementComponent;

import java.awt.image.BufferedImage;

public class Dash extends Ability {
    public Dash(PlayerTypeEntity owner, int x, int y, BufferedImage icon){
        super(owner, x, y, icon);
        cost = 1; //wird in den unter klassen überschrieben
        cooldown = 1500; //in Millisekunden
        duration = 100; //in Millisekunden
    }
    @Override
    public String getDescription(){ return "Dash";}
    @Override
    public void unlock(){ unlocked = true;}

    @Override
    public boolean use(){
        if (!super.use()){ return false; } //offcooldown check
        return true;
    }

    @Override
    public void effect() {
        int dir = owner.getDirection();
        double force = 7;
        if (dir == owner.NORTH ){
            owner.move(0, -force);
        }
        if (dir == owner.EAST ){
            owner.move(force, 0);
        }
        if (dir == owner.SOUTH ){
            owner.move(0, force);
        }
        if (dir == owner.WEST ){
            owner.move(-force, 0);
        }
    }
    public void end(){}
}
